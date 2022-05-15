package com.ruoxi.congestiontaxcalculator.service;

import com.ruoxi.congestiontaxcalculator.endpoint.model.TollFeeCalculationRequest;
import com.ruoxi.congestiontaxcalculator.exception.UnsupportedCityException;
import com.ruoxi.congestiontaxcalculator.model.TimedTollFee;
import com.ruoxi.congestiontaxcalculator.model.vehicle.VehicleType;
import com.ruoxi.congestiontaxcalculator.repositories.TollFeeTimedRuleRepository;
import com.ruoxi.congestiontaxcalculator.repositories.fee.TollFeeCity;
import com.ruoxi.congestiontaxcalculator.repositories.fee.TollFeeTimeSpan;
import com.ruoxi.congestiontaxcalculator.repositories.fee.TollFeeTimedRule;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class TollFeeService {

  private static final Duration FEE_INTERVAL_DURATION = Duration.ofHours(1L);
  private static final int MAX_FEE_PER_DAY = 60;
  private final Map<TollFeeCity, TollFeeTimedRule> allRules;
  private final DateValidator dateValidator;

  public TollFeeService(
      TollFeeTimedRuleRepository tollFeeTimedRuleRepository,
      DateValidator dateValidator,
      TollFeeTimeRuleInitializer tollFeeTimeRuleInitializer) {
    // initialize the toll fee database
    tollFeeTimeRuleInitializer.initializeTollFeeTimedRule();
    // one time load all rules into memory, as rules should not be changes too often
    // restart needed if rules are updated
    allRules =
        tollFeeTimedRuleRepository.findAll().stream()
            .collect(Collectors.toMap(TollFeeTimedRule::tollFeeCity, Function.identity()));
    this.dateValidator = dateValidator;
  }

  public int calculateTollFee(TollFeeCalculationRequest tollFeeCalculationRequest) {
    if (VehicleType.isTollFreeVehicle(tollFeeCalculationRequest.vehicleType())) {
      return 0;
    }
    // remove all 0-fee-date-time as they play nothing in the calculation
    // and sort the results in chronological order
    List<TimedTollFee> timedTollFees =
        tollFeeCalculationRequest.dates().stream()
            .map(
                dateTime ->
                    calculateSinglePassTollFee(
                        dateTime, TollFeeCity.safeMap(tollFeeCalculationRequest.tollFeeCity())))
            .filter(timedTollFee -> timedTollFee.fee() > 0)
            // sort the date to make them in chronological orders
            .sorted(Comparator.comparing(TimedTollFee::dateTime))
            .collect(Collectors.toList());
    return calculateTollFee(timedTollFees);
  }

  private int calculateTollFee(List<TimedTollFee> timedTollFees) {
    if (timedTollFees.isEmpty()) {
      return 0;
    }
    int totalFee = 0;
    int totalFeePerDay = 0;
    TimedTollFee intervalStartTime = timedTollFees.get(0);
    TimedTollFee intervalMaxFeeTime = timedTollFees.get(0);

    for (int i = 1; i < timedTollFees.size(); i++) {
      TimedTollFee thisFeeTime = timedTollFees.get(i);
      if (isFeeTimeSameDay(intervalStartTime.dateTime(), thisFeeTime.dateTime())) {
        if (isFeeTimeInInterval(intervalStartTime.dateTime(), thisFeeTime.dateTime())) {
          intervalMaxFeeTime =
              intervalMaxFeeTime.fee() > thisFeeTime.fee() ? intervalMaxFeeTime : thisFeeTime;
        } else {
          totalFeePerDay = totalFeePerDay + intervalMaxFeeTime.fee();
          intervalStartTime = thisFeeTime;
          intervalMaxFeeTime = thisFeeTime;
        }
      } else {
        totalFee = totalFee + Math.min(totalFeePerDay + intervalMaxFeeTime.fee(), MAX_FEE_PER_DAY);
        intervalStartTime = thisFeeTime;
        intervalMaxFeeTime = thisFeeTime;
        totalFeePerDay = 0;
      }
    }

    return totalFee + Math.min(totalFeePerDay + intervalMaxFeeTime.fee(), MAX_FEE_PER_DAY);
  }

  private boolean isFeeTimeSameDay(LocalDateTime intervalStartTime, LocalDateTime thisFeeTime) {
    return thisFeeTime.toLocalDate().equals(intervalStartTime.toLocalDate());
  }

  private boolean isFeeTimeInInterval(LocalDateTime intervalStartTime, LocalDateTime thisFeeTime) {
    return thisFeeTime.toLocalDate().equals(intervalStartTime.toLocalDate())
        && intervalStartTime.plus(FEE_INTERVAL_DURATION).isAfter(thisFeeTime);
  }

  private TimedTollFee calculateSinglePassTollFee(LocalDateTime dateTime, TollFeeCity city) {
    if (dateValidator.isTollFreeDate(dateTime)) {
      return new TimedTollFee(dateTime, 0);
    }

    // get toll fee rule for city, throw UnsupportedCityException if no rule found for the city
    TollFeeTimedRule tollFeeTimedRule = allRules.get(city);
    if (tollFeeTimedRule == null) {
      throw new UnsupportedCityException(city + " is not supported!");
    }

    return tollFeeTimedRule.tollFeeTimeSpans().stream()
        .filter(tollFeeTimeSpan -> isInTimeSpan(tollFeeTimeSpan, dateTime.toLocalTime()))
        .map(tollFeeTimeSpan -> new TimedTollFee(dateTime, tollFeeTimeSpan.fee()))
        .findFirst()
        .orElse(new TimedTollFee(dateTime, 0));
  }

  private boolean isInTimeSpan(TollFeeTimeSpan timeSpan, LocalTime time) {
    return time.compareTo(timeSpan.start()) >= 0 && time.compareTo(timeSpan.end()) < 0;
  }
}
