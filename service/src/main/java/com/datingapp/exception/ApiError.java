package com.datingapp.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiError {
	private final String message;
	private final LocalDateTime timeStamp;
	private final HttpStatus httpStatus;
}
