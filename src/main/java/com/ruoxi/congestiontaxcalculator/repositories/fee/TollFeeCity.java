package com.ruoxi.congestiontaxcalculator.repositories.fee;

import com.ruoxi.congestiontaxcalculator.exception.UnsupportedCityException;

public enum TollFeeCity {
  Gothenburg;

  public static TollFeeCity safeMap(String cityName) {
    return switch (cityName) {
      case "Gothenburg" -> Gothenburg;
      default -> throw new UnsupportedCityException(cityName + " is not supported!");
    };
  }
}
