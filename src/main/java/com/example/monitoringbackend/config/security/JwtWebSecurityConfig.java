package com.example.monitoringbackend.config.security;

import com.example.monitoringbackend.security.CustomUsernamePasswordAuthenticationProvider;
import com.example.monitoringbackend.web.filters.JwtFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class JwtWebSecurityConfig {

  private final CustomUsernamePasswordAuthenticationProvider authenticationProvider;
  private final JwtFilter jwtFilter;
  private final RestAuthenticationEntryPoint authenticationEntryPoint;
  private final RestAccessDeniedHandler accessDeniedHandler;

  @Value("${app.cors.allowed-origins}")
  private String allowedOrigins;

  public JwtWebSecurityConfig(
      CustomUsernamePasswordAuthenticationProvider authenticationProvider,
      JwtFilter jwtFilter,
      RestAuthenticationEntryPoint authenticationEntryPoint,
      RestAccessDeniedHandler accessDeniedHandler) {
    this.authenticationProvider = authenticationProvider;
    this.jwtFilter = jwtFilter;
    this.authenticationEntryPoint = authenticationEntryPoint;
    this.accessDeniedHandler = accessDeniedHandler;
  }

  /**
   * {@code JwtFilter} is a {@code @Component}, so Spring Boot would also register it as a plain
   * servlet filter running ahead of the security chain. Disabling that registration keeps
   * authentication inside the security chain, where the authorization rules apply.
   */
  @Bean
  public FilterRegistrationBean<JwtFilter> jwtFilterRegistration(JwtFilter filter) {
    FilterRegistrationBean<JwtFilter> registration = new FilterRegistrationBean<>(filter);
    registration.setEnabled(false);
    return registration;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedOrigins(List.of(allowedOrigins.split(",")));
    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    corsConfiguration.setAllowedHeaders(
        List.of("Authorization", "Content-Type", "Accept", "X-XSRF-TOKEN"));
    corsConfiguration.setAllowCredentials(true);
    corsConfiguration.setExposedHeaders(List.of("Authorization"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    return source;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(
            csrf ->
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                    .ignoringRequestMatchers("/api/**"))
        .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(
            authorizeHttpRequestsCustomizer ->
                authorizeHttpRequestsCustomizer
                    // CORS preflight carries no credentials and must stay open.
                    .requestMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll()
                    // Pre-authentication endpoints, the deployment health probe and the docs.
                    .requestMatchers(PublicEndpoints.patternsArray())
                    .permitAll()
                    // Cross-tenant listing of every component in the system.
                    .requestMatchers(HttpMethod.GET, "/api/component")
                    .hasAuthority("ROLE_ADMIN")
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(
            exceptionHandlingConfigurer ->
                exceptionHandlingConfigurer
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler))
        .sessionManagement(
            sessionManagementConfigurer ->
                sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  private static final class SpaCsrfTokenRequestHandler extends CsrfTokenRequestAttributeHandler {
    private final CsrfTokenRequestHandler delegate = new XorCsrfTokenRequestAttributeHandler();

    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        Supplier<CsrfToken> deferredCsrfToken) {
      delegate.handle(request, response, deferredCsrfToken);
      deferredCsrfToken.get();
    }

    @Override
    public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
      if (request.getHeader(csrfToken.getHeaderName()) != null) {
        return super.resolveCsrfTokenValue(request, csrfToken);
      }
      return delegate.resolveCsrfTokenValue(request, csrfToken);
    }
  }
}
