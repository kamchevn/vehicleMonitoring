package com.example.monitoringbackend.model.enumerations;

public enum ComponentType {
  // 🔧 Engine & Fluids
  ENGINE_OIL, // treba
  OIL_FILTER, // treba
  AIR_FILTER, // treba
  FUEL_FILTER, // treba
  SPARK_PLUG, // treba
  GLOW_PLUG, // treba
  CRANKCASE_BREAKER_FILTER, // bus/truck component, nema takov filter, vadi go i smeni go so "Pollen
  // Filter"
  ENGINE_BELT, // bus/truck component, treba
  TIMING_GEAR, // truck component

  // 🧪 Emissions
  OXYGEN_SENSOR, // sonda za gasovi, senzor za izduvni gasovi
  DPF_FILTER, // skapo za servis
  SCR, // bus/truck component
  CATALYTIC_CONVERTER, // ne treba
  EGR_VALVE, // ne treba

  // ❄ Cooling
  COOLANT, // treba
  WATER_PUMP, // treba
  THERMOSTAT, // treba

  // 🔋 Electrical
  BATTERY, // treba
  ALTERNATOR, // ne treba

  // 🔄 Transmission & Drivetrain
  TRANSMISSION_OIL, // ne e vo redoven servis
  TRANSMISSION_FILTER, // bus/truck component - ne e vo redoven servis
  CLUTCH,
  CLUTCH_CABLE, // motorcycle component
  GEARBOX,
  DIFFERENTIAL, // teshki vozila, odat vo par so Driveshaft (kardan)
  DRIVESHAFT, // bus/truck component
  DRIVE_CHAIN, // motorcycle component
  DRIVE_BELT, // motorcycle component
  DRIVE_OIL, // motorcycle component
  SPROCKETS, // motorcycle component
  CHAIN_LUBRICATION, // motorcycle component
  AXLE_OIL, // bus/truck component

  // 🛑 Braking
  BRAKE_PADS, // treba
  BRAKE_DISCS, // treba
  BRAKE_DRUMS, // bus component, treba
  BRAKE_FLUID, // treba
  BRAKE_CALIPER, // shepa, ne treba

  // 🛞 Suspension & Steering
  SHOCK_ABSORBER, // ne se vo redoven servis
  CONTROL_ARM, // ne se vo redoven servis
  BALL_JOINT, // ne se vo redoven servis
  TIE_ROD_END, // ne se vo redoven servis
  POWER_STEERING_FLUID, // ne se vo redoven servis
  FRONT_FORK_OIL, // motorcycle component
  REAR_SHOCK_ABSORBER, // motorcycle component
  STEERING_HEAD_BEARINGS, // motorcycle component
  SWINGARM_BEARINGS, // motorcycle component

  // 🛞 Wheels & Tires
  TIRES, // treba
  WHEEL_BEARING, // ne treba
  WHEEL_ALIGNMENT, // bus/truck component, ne treba

  // ❄ HVAC
  CABIN_AIR_FILTER, // "Pollen filter", treba
  HVAC_FILTER, // bus component
  AUXILIARY_FILTER, // bus/truck component

  // 🧰 Controls & Wear Items
  THROTTLE_CABLE, // motorcycle component
}
