package com.jrome.utils;

import com.google.gson.Gson;
import com.jrome.payload.OneDtoToRuleThemAll;

public class JSONConverter {

    /**
     * Util class for converting payloads to/from JSON.
     * It's more convenient like this, than to instantiate a new Gson object everytime we need to send
     * or receive a DTO
     */

    private static final Gson gson = new Gson();

    /**
     * Static method for converting to JSON
     */
    public static String dtoToJSON(OneDtoToRuleThemAll toJSON) {
        return gson.toJson(toJSON);
    }


    /**
     * Static generic method for converting from JSON
     * The usage of <T> makes it possible to pass and return whatever shit we want, in our case a DTO of our choice.
     * When we call the method, we do need specify which class we want it converted to, hence the "Class<T> clazz"
     */
    public static <T extends OneDtoToRuleThemAll> T jsonToDto(String fromJSON, Class<T> clazz) {
        return gson.fromJson(fromJSON, clazz);
    }
}
