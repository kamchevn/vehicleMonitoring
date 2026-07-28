package com.example.monitoringbackend.model;

import com.example.monitoringbackend.model.enumerations.Role;
import jakarta.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "vehicle_users")
public class User implements UserDetails {

  @Id private String username;

  private String password;

  @Column(nullable = false, unique = true)
  private String email;

  private String name;

  private String surname;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<Vehicle> vehicles;

  private boolean isAccountNonExpired = true;
  private boolean isAccountNonLocked = true;
  private boolean isCredentialsNonExpired = true;
  private boolean isEnabled = false;

  @Enumerated(value = EnumType.STRING)
  private Role role;

  public User() {}

  public User(
      String username, String password, String email, String name, String surname, Role role) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.name = name;
    this.surname = surname;
    this.role = role;
    this.isEnabled = false;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(role);
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return isAccountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return isAccountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return isCredentialsNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return isEnabled;
  }

  public void setEnabled(boolean enabled) {
    isEnabled = enabled;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public Role getRole() {
    return role;
  }
}
