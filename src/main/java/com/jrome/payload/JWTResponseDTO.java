package com.jrome.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JWTResponseDTO {

    private String tokenType;
    private String accessToken;

    @Override
    public String toString() {
        return  "\nToken type: " + tokenType +
                "\nAccess Token: " + accessToken;
    }
}
