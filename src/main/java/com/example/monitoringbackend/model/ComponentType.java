package com.example.monitoringbackend.model;

public enum ComponentType {
    // 🔧 Engine & Fluids
    ENGINE_OIL,
    OIL_FILTER,
    AIR_FILTER,
    FUEL_FILTER,
    SPARK_PLUG,
    GLOW_PLUG,
    CRANKCASE_BREAKER_FILTER, // bus/truck component
    ENGINE_BELT, // bus/truck component
    TIMING_GEAR, // truck component
    ENGINE_COOLANT, // motorcycle component

    // 🧪 Emissions
    OXYGEN_SENSOR,
    DPF_FILTER,
    SCR, // bus/truck component
    CATALYTIC_CONVERTER,
    EGR_VALVE,

    // ❄ Cooling
    COOLANT,
    WATER_PUMP,
    THERMOSTAT,

    // 🔋 Electrical
    BATTERY,
    ALTERNATOR,

    // 🔄 Transmission & Drivetrain
    TRANSMISSION_OIL,
    TRANSMISSION_FILTER, // bus/truck component
    CLUTCH,
    CLUTCH_CABLE, // motorcycle component
    GEARBOX,
    DIFFERENTIAL,
    DRIVESHAFT, // bus/truck component
    DRIVE_CHAIN, // motorcycle component
    DRIVE_BELT, // motorcycle component
    DRIVE_OIL, // motorcycle component
    SPROCKETS, // motorcycle component
    CHAIN_LUBRICATION, // motorcycle component
    AXLE_OIL, // bus/truck component

    // 🛑 Braking
    BRAKE_PADS,
    BRAKE_DISCS,
    BRAKE_DRUMS, // bus component
    BRAKE_FLUID,
    BRAKE_CALIPER,

    // 🛞 Suspension & Steering
    SHOCK_ABSORBER,
    CONTROL_ARM,
    BALL_JOINT,
    TIE_ROD_END,
    POWER_STEERING_FLUID,
    FRONT_FORK_OIL, // motorcycle component
    REAR_SHOCK_ABSORBER, // motorcycle component
    STEERING_HEAD_BEARINGS, // motorcycle component
    SWINGARM_BEARINGS, // motorcycle component

    // 🛞 Wheels & Tires
    TIRES,
    WHEEL_BEARING,
    WHEEL_ALIGNMENT, // bus/truck component

    // ❄ HVAC
    CABIN_AIR_FILTER,
    HVAC_FILTER, // bus component
    AUXILIARY_FILTER, // bus/truck component

    // 🧰 Controls & Wear Items
    THROTTLE_CABLE, // motorcycle component
}
