package org.ag.authenticationservice.exceptions;

public class UserAlreadyPresentException extends Exception {
    public UserAlreadyPresentException(String s) {
        super(s);
    }
}
