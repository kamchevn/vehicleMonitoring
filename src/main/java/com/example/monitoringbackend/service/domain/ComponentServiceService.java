package com.example.monitoringbackend.service.domain;

import com.example.monitoringbackend.model.ComponentService;
import com.example.monitoringbackend.model.ComponentServiceDetail;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

public interface ComponentServiceService {
  ComponentService findById(Long checkId);

  Page<ComponentService> findPage(
      UserDetails user, Long vehicleId, String checkType, Integer pageNum, Integer pageSize);

  void checkConditionForVehicle(
      Long id,
      String checkType,
      String note,
      LocalDateTime checkTime,
      List<ComponentServiceDetail> componentDetails);
}
