package com.jrome.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO  {

    private String username;
    private String password;

    @Override
    public String toString() {
        return  "\nUsername: " + username +
                "\nPassword: " + password;
    }
}
