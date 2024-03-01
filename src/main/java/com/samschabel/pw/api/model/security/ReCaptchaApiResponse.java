package com.samschabel.pw.api.model.security;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReCaptchaApiResponse {

    boolean success;
    Double score;
    String action;
    OffsetDateTime challenge_ts;
    String hostname;
    @JsonAlias("error-codes")
    String[] errorCodes;

}
