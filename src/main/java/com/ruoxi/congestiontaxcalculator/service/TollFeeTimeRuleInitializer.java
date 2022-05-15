package com.ruoxi.congestiontaxcalculator.service;

import com.ruoxi.congestiontaxcalculator.repositories.TollFeeTimedRuleRepository;
import com.ruoxi.congestiontaxcalculator.repositories.fee.TollFeeCity;
import com.ruoxi.congestiontaxcalculator.repositories.fee.TollFeeTimeSpan;
import com.ruoxi.congestiontaxcalculator.repositories.fee.TollFeeTimedRule;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Component;

// this is just a helper for initializing toll fee rule database
// in real scenario there would be a call to some endpoint to initialize the data
@Component
public class TollFeeTimeRuleInitializer {

  private final TollFeeTimedRuleRepository tollFeeTimedRuleRepository;

  public TollFeeTimeRuleInitializer(TollFeeTimedRuleRepository tollFeeTimedRuleRepository) {
    this.tollFeeTimedRuleRepository = tollFeeTimedRuleRepository;
  }

  public void initializeTollFeeTimedRule() {
    initializeGothenburg();
  }

  private void initializeGothenburg() {

    // 06:00–06:29	SEK 8
    // 06:30–06:59	SEK 13
    // 07:00–07:59	SEK 18
    // 08:00–08:29	SEK 13
    // 08:30–14:59	SEK 8
    // 15:00–15:29	SEK 13
    // 15:30–16:59	SEK 18
    // 17:00–17:59	SEK 13
    // 18:00–18:29	SEK 8
    // 18:30–05:59	SEK 0
    List<TollFeeTimeSpan> tollFeeTimeSpans =
        List.of(
            // when compare with the start end date time,
            // the starting time is included and the end date time is excluded
            new TollFeeTimeSpan(LocalTime.of(6, 0, 0), LocalTime.of(6, 30, 0), 8),
            new TollFeeTimeSpan(LocalTime.of(6, 30, 0), LocalTime.of(7, 0, 0), 13),
            new TollFeeTimeSpan(LocalTime.of(7, 0, 0), LocalTime.of(8, 0, 0), 18),
            new TollFeeTimeSpan(LocalTime.of(8, 0, 0), LocalTime.of(8, 30, 0), 13),
            new TollFeeTimeSpan(LocalTime.of(8, 30, 0), LocalTime.of(15, 0, 0), 8),
            new TollFeeTimeSpan(LocalTime.of(15, 0, 0), LocalTime.of(15, 30, 0), 13),
            new TollFeeTimeSpan(LocalTime.of(15, 30, 0), LocalTime.of(17, 0, 0), 18),
            new TollFeeTimeSpan(LocalTime.of(17, 0, 0), LocalTime.of(18, 0, 0), 13),
            new TollFeeTimeSpan(LocalTime.of(18, 0, 0), LocalTime.of(18, 30, 0), 8));
    tollFeeTimedRuleRepository.insert(
        new TollFeeTimedRule(null, null, tollFeeTimeSpans, TollFeeCity.Gothenburg));
  }
}
