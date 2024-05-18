package org.ag.authenticationservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.ag.authenticationservice.enums.SessionState;

@Getter
@Setter
@Entity
public class Session extends BaseModel{

    @ManyToOne
    private User user;

    @Enumerated(EnumType.ORDINAL)
    private SessionState sessionState;
    private String token;

}
