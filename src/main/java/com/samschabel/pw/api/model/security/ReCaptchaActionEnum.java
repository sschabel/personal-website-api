package com.samschabel.pw.api.model.security;

import lombok.Getter;

@Getter
public enum ReCaptchaActionEnum {

    LOGIN("LOGIN", 0.5);

    private String action;
    private Double threshold;

    ReCaptchaActionEnum(String action, Double threshold) {
        this.action = action;
        this.threshold = threshold;
    }

    @Override
    public String toString() {
        return this.action;
    }
}
