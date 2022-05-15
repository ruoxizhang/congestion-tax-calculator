package com.ruoxi.congestiontaxcalculator.endpoint;

import com.ruoxi.congestiontaxcalculator.config.TestContextExtension;
import com.ruoxi.congestiontaxcalculator.endpoint.model.TollFeeCalculationRequest;
import com.ruoxi.congestiontaxcalculator.exception.ErrorModel;
import com.ruoxi.congestiontaxcalculator.model.vehicle.VehicleType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class, TestContextExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TollFeeEndpointTest {

  private static final DateTimeFormatter dateTimeFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Autowired TestRestTemplate restTemplate;
  @LocalServerPort private int port;

  @Test
  @DisplayName("Test with provided dates")
  void testHappyFlowCalculateTollFee() {
    final String BASE_URI = "http://localhost:" + port + "/";

    List<String> dateTimeStrings =
        List.of(
            "2013-01-14 21:00:00",
            "2013-01-15 21:00:00",
            "2013-02-07 06:23:27",
            "2013-02-07 15:27:00",
            "2013-02-08 06:27:00",
            "2013-02-08 06:20:27",
            "2013-02-08 14:35:00",
            "2013-02-08 15:29:00",
            "2013-02-08 15:47:00",
            "2013-02-08 16:01:00",
            "2013-02-08 16:48:00",
            "2013-02-08 17:49:00",
            "2013-02-08 18:29:00",
            "2013-02-08 18:35:00",
            "2013-03-26 14:25:00",
            "2013-03-28 14:07:27");
    List<LocalDateTime> dateTimes = parseDateTimes(dateTimeFormatter, dateTimeStrings);
    TollFeeCalculationRequest tollFeeCalculationRequest =
        new TollFeeCalculationRequest(dateTimes, VehicleType.Car, "Gothenburg");
    Integer calculatedTollFee =
        restTemplate.postForObject(
            BASE_URI + "/api/tollFee", tollFeeCalculationRequest, Integer.class);
    Assertions.assertEquals(89, calculatedTollFee);
  }

  @Test
  @DisplayName("Test with one date")
  void TestWithOneDate() {
    final String BASE_URI = "http://localhost:" + port + "/";

    List<String> dateTimeStrings = List.of("2013-02-07 06:23:27");
    List<LocalDateTime> dateTimes = parseDateTimes(dateTimeFormatter, dateTimeStrings);
    TollFeeCalculationRequest tollFeeCalculationRequest =
        new TollFeeCalculationRequest(dateTimes, VehicleType.Car, "Gothenburg");
    Integer calculatedTollFee =
        restTemplate.postForObject(
            BASE_URI + "/api/tollFee", tollFeeCalculationRequest, Integer.class);
    Assertions.assertEquals(8, calculatedTollFee);
  }

  @Test
  @DisplayName("Test with free vehicle")
  void testWithFreeVehicle() {
    final String BASE_URI = "http://localhost:" + port + "/";

    List<String> dateTimeStrings = List.of("2013-02-07 06:23:27");
    List<LocalDateTime> dateTimes = parseDateTimes(dateTimeFormatter, dateTimeStrings);
    TollFeeCalculationRequest tollFeeCalculationRequest =
        new TollFeeCalculationRequest(dateTimes, VehicleType.Emergency, "Gothenburg");
    Integer calculatedTollFee =
        restTemplate.postForObject(
            BASE_URI + "/api/tollFee", tollFeeCalculationRequest, Integer.class);
    Assertions.assertEquals(0, calculatedTollFee);
  }

  @Test
  @DisplayName("Test with unknown city")
  void testWithUnknownCity() {
    final String BASE_URI = "http://localhost:" + port + "/";

    List<String> dateTimeStrings = List.of("2013-01-14 21:00:00");
    List<LocalDateTime> dateTimes = parseDateTimes(dateTimeFormatter, dateTimeStrings);
    TollFeeCalculationRequest tollFeeCalculationRequest =
        new TollFeeCalculationRequest(dateTimes, VehicleType.Car, "Uppsala");

    ResponseEntity<ErrorModel> errorResponse =
        restTemplate.postForEntity(
            BASE_URI + "/api/tollFee", tollFeeCalculationRequest, ErrorModel.class);
    Assertions.assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
    Assertions.assertEquals(
        "Uppsala is not supported!", Objects.requireNonNull(errorResponse.getBody()).message());
  }

  @NotNull
  private List<LocalDateTime> parseDateTimes(
      DateTimeFormatter dateTimeFormatter, List<String> dateTimeStrings) {
    return dateTimeStrings.stream()
        .map(dateTimeString -> LocalDateTime.parse(dateTimeString, dateTimeFormatter))
        .collect(Collectors.toList());
  }
}
