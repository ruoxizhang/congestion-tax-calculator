package com.ruoxi.congestiontaxcalculator.service;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import org.springframework.stereotype.Service;

@Service
public class DateValidator {

  public Boolean isTollFreeDate(LocalDateTime date) {
    int year = date.getYear();
    Month month = date.getMonth();
    DayOfWeek dayOfWeek = date.getDayOfWeek();
    int dayOfMonth = date.getDayOfMonth();

    if (dayOfWeek == SATURDAY || dayOfWeek == SUNDAY) {
      return true;
    }

    if (year == 2013) {
      return (month == Month.JANUARY && dayOfMonth == 1)
          || (month == Month.MARCH && (dayOfMonth == 28 || dayOfMonth == 29))
          || (month == Month.APRIL && (dayOfMonth == 1 || dayOfMonth == 30))
          || (month == Month.MAY && (dayOfMonth == 1 || dayOfMonth == 8 || dayOfMonth == 9))
          || (month == Month.JUNE && (dayOfMonth == 5 || dayOfMonth == 6 || dayOfMonth == 21))
          || (month == Month.JULY)
          || (month == Month.NOVEMBER && dayOfMonth == 1)
          || (month == Month.DECEMBER
              && (dayOfMonth == 24 || dayOfMonth == 25 || dayOfMonth == 26 || dayOfMonth == 31));
    }
    return false;
  }
}
