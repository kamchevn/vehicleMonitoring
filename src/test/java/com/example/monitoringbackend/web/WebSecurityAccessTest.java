package com.example.monitoringbackend.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.monitoringbackend.model.User;
import com.example.monitoringbackend.model.Vehicle;
import com.example.monitoringbackend.model.enumerations.Condition;
import com.example.monitoringbackend.model.enumerations.IntervalInsertPeriod;
import com.example.monitoringbackend.model.enumerations.Role;
import com.example.monitoringbackend.model.enumerations.VehicleType;
import com.example.monitoringbackend.repository.UserRepository;
import com.example.monitoringbackend.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Verifies the HTTP surface is closed by default and that object level ownership is enforced. These
 * assertions are the regression net for the Phase 0 access control fixes.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@Transactional
class WebSecurityAccessTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private UserRepository userRepository;
  @Autowired private VehicleRepository vehicleRepository;

  private Long ownedVehicleId;

  @BeforeEach
  void seedData() {
    User owner = persistedUser("owner", Role.ROLE_USER);
    persistedUser("intruder", Role.ROLE_USER);
    persistedUser("boss", Role.ROLE_ADMIN);

    Vehicle vehicle =
        vehicleRepository.save(
            new Vehicle(
                "Golf",
                2016,
                120000,
                VehicleType.CAR,
                null,
                null,
                null,
                Condition.GOOD,
                IntervalInsertPeriod.WEEKLY,
                owner));
    ownedVehicleId = vehicle.getId();
  }

  private User persistedUser(String username, Role role) {
    User user = new User(username, "encoded", username + "@example.com", "N", "S", role);
    user.setEnabled(true);
    return userRepository.save(user);
  }

  // --- deny by default -------------------------------------------------------

  @Test
  @WithAnonymousUser
  void vehicleList_requiresAuthentication() throws Exception {
    mockMvc.perform(get("/api/vehicle")).andExpect(status().isUnauthorized());
  }

  @Test
  @WithAnonymousUser
  void vehicleById_requiresAuthentication() throws Exception {
    mockMvc.perform(get("/api/vehicle/" + ownedVehicleId)).andExpect(status().isUnauthorized());
  }

  @Test
  @WithAnonymousUser
  void componentPage_requiresAuthentication() throws Exception {
    mockMvc
        .perform(get("/api/component/page").param("vehicleId", ownedVehicleId.toString()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithAnonymousUser
  void serviceCreation_requiresAuthentication() throws Exception {
    mockMvc
        .perform(
            post("/api/service/" + ownedVehicleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"serviceType\":\"REGULAR\",\"componentDetails\":[]}"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithAnonymousUser
  void componentTemplates_requireAuthentication() throws Exception {
    mockMvc.perform(get("/api/template/CAR")).andExpect(status().isUnauthorized());
  }

  @Test
  @WithAnonymousUser
  void vehicleDeletion_requiresAuthentication() throws Exception {
    mockMvc
        .perform(delete("/api/vehicle/delete/" + ownedVehicleId))
        .andExpect(status().isUnauthorized());

    assertTrue(vehicleRepository.findById(ownedVehicleId).isPresent());
  }

  // --- endpoints that must stay reachable without a token --------------------

  @Test
  @WithAnonymousUser
  void healthEndpoint_staysPublicForTheDeploymentProbe() throws Exception {
    mockMvc.perform(get("/actuator/health")).andExpect(status().isOk());
  }

  @Test
  @WithAnonymousUser
  void login_staysPublic() throws Exception {
    mockMvc
        .perform(
            post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"owner\",\"password\":\"wrong\"}"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").exists());
  }

  // The next three intentionally carry no @WithMockUser/@WithAnonymousUser: those pre-populate the
  // security context, which would make JwtFilter skip token processing entirely.

  @Test
  void anExpiredTokenDoesNotBlockLoggingInAgain() throws Exception {
    // The SPA attaches whatever token it holds to every call, including the login request.
    mockMvc
        .perform(
            post("/api/user/login")
                .header("Authorization", "Bearer not-a-real-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"owner\",\"password\":\"wrong\"}"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("Invalid username or password."));
  }

  @Test
  void healthProbeIgnoresAStaleToken() throws Exception {
    mockMvc
        .perform(get("/actuator/health").header("Authorization", "Bearer not-a-real-token"))
        .andExpect(status().isOk());
  }

  @Test
  void garbageTokenOnAProtectedEndpoint_isRejected() throws Exception {
    mockMvc
        .perform(get("/api/vehicle").header("Authorization", "Bearer not-a-real-token"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithAnonymousUser
  void registration_staysPublicAndStillValidatesInput() throws Exception {
    mockMvc
        .perform(
            post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"\",\"password\":\"x\"}"))
        .andExpect(status().isBadRequest());
  }

  // --- destructive verbs -----------------------------------------------------

  @Test
  @WithMockUser(username = "owner")
  void deletingAVehicleViaGet_isNoLongerPossible() throws Exception {
    mockMvc
        .perform(get("/api/vehicle/delete/" + ownedVehicleId))
        .andExpect(status().isMethodNotAllowed());

    assertTrue(vehicleRepository.findById(ownedVehicleId).isPresent());
  }

  @Test
  @WithMockUser(username = "owner")
  void deletingAComponentViaGet_isNoLongerPossible() throws Exception {
    mockMvc.perform(get("/api/component/delete/1")).andExpect(status().isMethodNotAllowed());
  }

  // --- object level ownership ------------------------------------------------

  @Test
  @WithMockUser(username = "owner")
  void owner_canReadOwnVehicle() throws Exception {
    mockMvc.perform(get("/api/vehicle/" + ownedVehicleId)).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "intruder")
  void otherUser_cannotReadSomebodyElsesVehicle() throws Exception {
    mockMvc.perform(get("/api/vehicle/" + ownedVehicleId)).andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(username = "intruder")
  void otherUser_cannotDeleteSomebodyElsesVehicle() throws Exception {
    mockMvc
        .perform(delete("/api/vehicle/delete/" + ownedVehicleId))
        .andExpect(status().isForbidden());

    assertTrue(vehicleRepository.findById(ownedVehicleId).isPresent());
  }

  @Test
  @WithMockUser(username = "intruder")
  void otherUser_cannotListComponentsOfSomebodyElsesVehicle() throws Exception {
    mockMvc
        .perform(get("/api/component/findByVehicle/" + ownedVehicleId))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(username = "intruder")
  void otherUser_cannotPageComponentsOfSomebodyElsesVehicle() throws Exception {
    mockMvc
        .perform(get("/api/component/page").param("vehicleId", ownedVehicleId.toString()))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(username = "intruder")
  void otherUser_cannotInsertIntervalsForSomebodyElsesVehicle() throws Exception {
    mockMvc
        .perform(
            post("/api/vehicle/insertInterval/" + ownedVehicleId)
                .param("unitType", "KILOMETERS")
                .param("amount", "100")
                .param("insertTime", "2026-01-01T10:00:00")
                .param("late", "false"))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(username = "intruder")
  void otherUser_cannotServiceSomebodyElsesVehicle() throws Exception {
    mockMvc
        .perform(
            post("/api/service/" + ownedVehicleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"serviceType\":\"REGULAR\",\"componentDetails\":[]}"))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(username = "boss", authorities = "ROLE_ADMIN")
  void administrator_mayReadAnyVehicle() throws Exception {
    mockMvc.perform(get("/api/vehicle/" + ownedVehicleId)).andExpect(status().isOk());
  }

  // --- role gated endpoints -------------------------------------------------

  @Test
  @WithMockUser(username = "owner")
  void crossTenantComponentList_isDeniedToRegularUsers() throws Exception {
    mockMvc.perform(get("/api/component")).andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(username = "boss", authorities = "ROLE_ADMIN")
  void crossTenantComponentList_isAllowedForAdministrators() throws Exception {
    mockMvc.perform(get("/api/component")).andExpect(status().isOk());
  }

  // --- form binding on the new verbs ---------------------------------------

  @Test
  @WithMockUser(username = "owner")
  void editingAVehicleWorksOverPutWithFormEncodedBody() throws Exception {
    mockMvc
        .perform(
            // Sent as a real request body rather than pre-parsed parameters so the test covers
            // the form content filter that makes @RequestParam work on PUT.
            put("/api/vehicle/edit/" + ownedVehicleId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(
                    "name=Golf+renamed&year=2016&totalKilometers=125000"
                        + "&type=CAR&condition=GOOD&insertPeriodType=WEEKLY"))
        .andExpect(status().isOk());

    assertEquals(
        "Golf renamed", vehicleRepository.findById(ownedVehicleId).orElseThrow().getName());
  }
}
