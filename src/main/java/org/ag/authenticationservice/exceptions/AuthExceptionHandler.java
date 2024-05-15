package org.ag.authenticationservice.exceptions;

import org.ag.authenticationservice.dtos.ResponseStatus;
import org.ag.authenticationservice.dtos.SignUpResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler( { UserAlreadyPresentException.class , UserNotFoundException.class, PasswordMismatchException.class})
    public ResponseEntity<ErrorResponse> handleCheckedException(final Exception e) {
        ErrorResponse error = ErrorResponse.builder()
                .responseStatus(ResponseStatus.FAILURE)
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler( { RuntimeException.class})
    public ResponseEntity<ErrorResponse> handleException(final RuntimeException e) {
        ErrorResponse error = ErrorResponse.builder()
                .responseStatus(ResponseStatus.FAILURE)
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
