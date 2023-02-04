package com.youngpopeugene.vkservice.util;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Validator {
    public static ArrayList<String> validateUserId(String userId) {
        ArrayList<String> userIdErrors = new ArrayList<>();
        try {
            int id = Integer.parseInt(userId);
            if (id < 1) userIdErrors.add("user_id have to be greater than 0");
        } catch (Exception e) {
            userIdErrors.add("user_id have to be integer");
        }
        return userIdErrors;
    }

    public static ArrayList<String> validateGroupId(String groupId) {
        ArrayList<String> groupIdErrors = new ArrayList<>();
        try {
            int id = Integer.parseInt(groupId);
            if (id < 1) groupIdErrors.add("group_id have to be greater than 0");
        } catch (Exception e) {
            groupIdErrors.add("group_id have to be integer");
        }
        return groupIdErrors;
    }

    public static ArrayList<String> validateEmail(String email) {
        ArrayList<String> emailErrors = new ArrayList<>();
        String pattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        if(!Pattern.compile(pattern).matcher(email).matches()){
            emailErrors.add("invalid format of email");
        }
        return emailErrors;
    }

    public static ArrayList<String> validatePassword(String password) {
        ArrayList<String> passwordErrors = new ArrayList<>();
        if(password.contains(" ")){
            passwordErrors.add("whitespaces are not allowed in the password");
        }
        if(password.length() < 8 || password.length() > 30){
            passwordErrors.add("password length have to be 8-30 characters");
        }
        return passwordErrors;
    }

    public static String prepareValidationErrorMessage(ArrayList<String> errors) {
        String stringErrors = String.join(", ", errors);
        return ("Validation error: " + stringErrors);
    }
}
