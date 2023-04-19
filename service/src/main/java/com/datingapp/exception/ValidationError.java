package com.datingapp.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class ValidationError {
	private List<CustomFieldError> errors = new ArrayList<>();

	public void addFieldError(String field, String message) {
		CustomFieldError error = new CustomFieldError(field, message);
		errors.add(error);
	}

	@Data
	@AllArgsConstructor
	private static class CustomFieldError {
		private String field;
		private String message;
	}
}
