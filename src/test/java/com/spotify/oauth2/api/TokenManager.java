package com.spotify.oauth2.api;

import com.spotify.oauth2.utils.ConfigLoader;
import io.restassured.response.Response;

import java.time.Instant;
import java.util.HashMap;

import static com.spotify.oauth2.api.RestResource.postAccount;

public class TokenManager {


        private static String access_token;
        private static Instant expiry_time;

    public synchronized static String getToken() {

        try {
            if (expiry_time == null || Instant.now().isAfter(expiry_time)) {
                System.out.println("Refreshing token...");
                Response response = renewToken();
                access_token = response.path("access_token");
                int expiry_duration_in_seconds = response.path("expires_in");
                expiry_time = Instant.now().plusSeconds(expiry_duration_in_seconds - 300);
            } else {
                System.out.println("Token is good to use");
            }
        } catch (Exception e) {
            throw new RuntimeException("Abort! Failed to get a token");
        }

        return access_token;
    }

    private static Response renewToken() {

        HashMap<String, String > formParams = new HashMap<>();
        formParams.put("grant_type", ConfigLoader.getInstance().getGrantType());
        formParams.put("refresh_token", ConfigLoader.getInstance().getRefreshToken());

        HashMap<String, String> headerParams = new HashMap<>();
        headerParams.put("Authorization", ConfigLoader.getInstance().getAuthorization());
        headerParams.put("Content-Type", ConfigLoader.getInstance().getContentType());

        Response response = postAccount(headerParams, formParams);

        if (response.statusCode() != 200) {
            throw new RuntimeException("Abort! Refresh token failed");
        }
            return response;

    }
}
