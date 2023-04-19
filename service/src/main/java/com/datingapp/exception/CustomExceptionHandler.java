package com.datingapp.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class CustomExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MessageSource messageSource;

    @Autowired
    public CustomExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ValidationError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        logger.error("A validation error occurred: ", ex);
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        return new ResponseEntity<>(processFieldErrors(fieldErrors), BAD_REQUEST);
    }

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException ex){
       ApiError apiError = new ApiError(ex.getMessage(), LocalDateTime.now(), BAD_REQUEST);

        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    private ValidationError processFieldErrors(List<FieldError> fieldErrors) {
        ValidationError validationError = new ValidationError();

        for (FieldError fieldError : fieldErrors) {
            validationError.addFieldError(fieldError.getField(), messageSource.getMessage(fieldError, Locale.getDefault()));
        }

        return validationError;
    }

}

