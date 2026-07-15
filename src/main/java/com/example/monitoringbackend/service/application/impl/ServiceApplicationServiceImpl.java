package com.example.monitoringbackend.service.application.impl;

import com.example.monitoringbackend.model.ComponentService;
import com.example.monitoringbackend.model.dto.DisplayServiceDto;
import com.example.monitoringbackend.service.application.ServiceApplicationService;
import com.example.monitoringbackend.service.domain.ComponentServiceService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class ServiceApplicationServiceImpl implements ServiceApplicationService {
  private final ComponentServiceService componentServiceService;

  public ServiceApplicationServiceImpl(ComponentServiceService componentServiceService) {
    this.componentServiceService = componentServiceService;
  }

  @Override
  public DisplayServiceDto findById(Long checkId) {
    return DisplayServiceDto.from(componentServiceService.findById(checkId));
  }

  @Override
  public Page<DisplayServiceDto> findPage(
      UserDetails user, Long vehicleId, String checkType, Integer pageNum, Integer pageSize) {
    Page<ComponentService> checks =
        componentServiceService.findPage(user, vehicleId, checkType, pageNum, pageSize);
    return checks.map(DisplayServiceDto::from);
  }
}
