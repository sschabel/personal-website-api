package com.samschabel.pw.api.model.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReCaptchaStatus {

    boolean thresholdMet;
    ReCaptchaActionEnum action;

}
