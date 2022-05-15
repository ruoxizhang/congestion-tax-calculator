package com.ruoxi.congestiontaxcalculator.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class RestExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

  @ExceptionHandler(UnsupportedCityException.class)
  ResponseEntity<ErrorModel> handleNotFoundException(Exception e) {
    logger.info("Unknown city for congestion fee, {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorModel(e.getMessage()));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorModel> handleException(Exception e) {
    logger.error("Unhandled exception: {}", e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorModel(e.getMessage()));
  }
}
