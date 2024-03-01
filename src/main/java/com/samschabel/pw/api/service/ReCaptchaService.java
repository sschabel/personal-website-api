package com.samschabel.pw.api.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.samschabel.pw.api.exception.ApiException;
import com.samschabel.pw.api.model.security.ReCaptchaActionEnum;
import com.samschabel.pw.api.model.security.ReCaptchaApiResponse;
import com.samschabel.pw.api.model.security.ReCaptchaStatus;

import io.jsonwebtoken.lang.Arrays;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Service
public class ReCaptchaService {

    @Value("${pw-api.recaptcha.secret}")
    private String secret;
    private static final String G_RECAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify";
    private static final Double DEFAULT_RECAPTCHA_THRESHOLD = 0.5;

    public ReCaptchaStatus verifyToken(ReCaptchaActionEnum action, String token) throws HttpMessageNotWritableException, IOException {
        // for application/x-www-form-urlencoded, Spring uses FormHttpMessageConverter
        // which needs MultiValueMap to properly write the request body
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.set("secret", secret);
        requestBody.set("response", token);

        ResponseEntity<ReCaptchaApiResponse> response = RestClient.create()
                .post()
                .uri(G_RECAPTCHA_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(requestBody)
                .retrieve()
                .toEntity(ReCaptchaApiResponse.class);

        ReCaptchaApiResponse responseBody = response.getBody();

        if (response.getStatusCode().is2xxSuccessful() && response.getBody().isSuccess()) {
            return new ReCaptchaStatus(meetsReCaptchaThreshold(action, responseBody), action);
        } else {
            throw new ApiException(createReCaptchaErrorMessage(responseBody));
        }
    }

    private boolean meetsReCaptchaThreshold(ReCaptchaActionEnum action, ReCaptchaApiResponse response) {
        if(ReCaptchaActionEnum.LOGIN.equals(action)) {
            return response.getScore() > ReCaptchaActionEnum.LOGIN.getThreshold();
        } else {
            return response.getScore() > DEFAULT_RECAPTCHA_THRESHOLD;
        }
        
    }

    private String createReCaptchaErrorMessage(ReCaptchaApiResponse responseBody) {
        StringBuilder builder = new StringBuilder();
        builder.append("Unsuccessful ReCaptcha verification: \n");
        Arrays.asList(responseBody.getErrorCodes()).forEach((errorCode) -> builder.append(errorCode + "\n"));
        return builder.toString();
    }

}
