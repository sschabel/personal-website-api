package com.samschabel.pw.api.exception;

import org.springframework.http.HttpStatus;

public class DataNotFoundException extends ApiException {

    public DataNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
