package net.azurewebsites.sportywarsaw.models;

import com.google.gson.annotations.SerializedName;

public class AccessTokenModel {

    @SerializedName("access_token")
    private String accessToken;

    private String tokenType;

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        // OAuth requires uppercase Authorization HTTP header value for token type
        if ( ! Character.isUpperCase(tokenType.charAt(0))) {
            tokenType =
                    Character
                            .toString(tokenType.charAt(0))
                            .toUpperCase() + tokenType.substring(1);
        }
        return tokenType;
    }
}
