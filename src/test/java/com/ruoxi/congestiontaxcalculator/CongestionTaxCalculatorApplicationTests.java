package com.ruoxi.congestiontaxcalculator;

import com.ruoxi.congestiontaxcalculator.config.TestContextExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class, TestContextExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CongestionTaxCalculatorApplicationTests {

  @Test
  void contextLoads() {}
}
