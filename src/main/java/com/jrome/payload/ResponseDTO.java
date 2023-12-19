package com.jrome.payload;

public class ResponseDTO {
    private String tokenType;
    private String accessToken;

    public ResponseDTO() {
    }

    public ResponseDTO(String tokenType, String accessToken) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return  "\nToken type: " + tokenType +
                "\nAccess Token: " + accessToken;
    }
}
