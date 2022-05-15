package com.ruoxi.congestiontaxcalculator.repositories.fee;

import java.time.LocalTime;
import org.springframework.data.mongodb.core.mapping.Field;

public record TollFeeTimeSpan(
    @Field(TOLL_FEE_TIME_START) LocalTime start,
    @Field(TOLL_FEE_TIME_END) LocalTime end,
    @Field(TOLL_FEE_AMOUNT) int fee) {

  public static final String TOLL_FEE_TIME_START = "start";
  public static final String TOLL_FEE_TIME_END = "end";
  public static final String TOLL_FEE_AMOUNT = "fee";
}
