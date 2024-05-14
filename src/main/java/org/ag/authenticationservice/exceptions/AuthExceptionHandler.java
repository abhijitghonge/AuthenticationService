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

    @ExceptionHandler( { UserAlreadyPresentException.class })
    public ResponseEntity<ErrorResponse> handleUserAlreadyPresentException(final UserAlreadyPresentException e) {
        ErrorResponse error = ErrorResponse.builder()
                .responseStatus(ResponseStatus.FAILURE)
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
