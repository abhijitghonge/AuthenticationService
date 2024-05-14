package org.ag.authenticationservice.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.ag.authenticationservice.models.Role;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
public class SignUpResponseDto {

    private String email;
    private Set<Role> roles;
    private ResponseStatus status;
    private String failureMessage;
}
