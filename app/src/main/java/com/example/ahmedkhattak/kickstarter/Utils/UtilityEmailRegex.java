package com.example.ahmedkhattak.kickstarter.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ahmed Khattak on 24/11/2017.
 * From https://stackoverflow.com/a/8204716
 */

public class UtilityEmailRegex {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
}
