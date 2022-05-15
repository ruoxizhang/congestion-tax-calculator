package com.ruoxi.congestiontaxcalculator.model.vehicle;

import java.util.List;

public enum VehicleType {
  Motorcycle,
  Motorbike,
  Tractor,
  Emergency,
  Diplomat,
  Foreign,
  Military,
  Car,
  Unknown;

  private static List<VehicleType> tollFeeFreeVehicles =
      List.of(Motorcycle, Tractor, Emergency, Diplomat, Foreign, Military);

  public static boolean isTollFreeVehicle(VehicleType vehicleType) {
    return tollFeeFreeVehicles.contains(vehicleType);
  }
}
