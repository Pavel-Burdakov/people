package com.example.servingwebcontent.exception;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class EntityAlreadyExist extends Exception {
    public EntityAlreadyExist(String email) {
        super("entity with email already exists: " + email);
    }
}
