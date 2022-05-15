package com.ruoxi.congestiontaxcalculator.repositories.fee;

import java.time.Instant;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("tollFeeTimedRule")
@TypeAlias("TollFeeTimedRule")
public record TollFeeTimedRule(
    @Id String id,
    @CreatedDate Instant createdDate,
    @Field(TOLL_FEE_TIME_SPANS) List<TollFeeTimeSpan> tollFeeTimeSpans,
    @Field(TOLL_FEE_CITY) TollFeeCity tollFeeCity) {

  public static final String TOLL_FEE_TIME_SPANS = "tollFeeTimeSpans";
  public static final String TOLL_FEE_CITY = "tollFeeCity";
}
