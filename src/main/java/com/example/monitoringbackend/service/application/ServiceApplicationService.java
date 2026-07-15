package com.example.monitoringbackend.service.application;

import com.example.monitoringbackend.model.dto.DisplayServiceDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

public interface ServiceApplicationService {
  DisplayServiceDto findById(Long checkId);

  Page<DisplayServiceDto> findPage(
      UserDetails user, Long vehicleId, String checkType, Integer pageNum, Integer pageSize);
}
