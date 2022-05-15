package com.ruoxi.congestiontaxcalculator.endpoint;

import com.ruoxi.congestiontaxcalculator.endpoint.model.TollFeeCalculationRequest;
import com.ruoxi.congestiontaxcalculator.service.TollFeeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class TollFeeEndpoint {

  private final TollFeeService tollFeeService;

  public TollFeeEndpoint(TollFeeService tollFeeService) {
    this.tollFeeService = tollFeeService;
  }

  @PostMapping("/tollFee")
  public int calculateTollFee(@RequestBody TollFeeCalculationRequest tollFeeCalculationRequest) {
    return tollFeeService.calculateTollFee(tollFeeCalculationRequest);
  }
}
