package com.ruoxi.congestiontaxcalculator.endpoint.model;

import com.ruoxi.congestiontaxcalculator.model.vehicle.VehicleType;
import java.time.LocalDateTime;
import java.util.List;

public record TollFeeCalculationRequest(
    List<LocalDateTime> dates, VehicleType vehicleType, String tollFeeCity) {}
