package org.ag.authenticationservice.exceptions;

import lombok.Builder;
import lombok.Data;
import org.ag.authenticationservice.dtos.ResponseStatus;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ErrorResponse {
    private String message;
    private ResponseStatus responseStatus; // Optional, for specific error codes
}
