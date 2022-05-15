package com.ruoxi.congestiontaxcalculator.model;

import java.time.LocalDateTime;

public record TimedTollFee(LocalDateTime dateTime, int fee) {}
