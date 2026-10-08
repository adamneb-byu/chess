package handler;

import data.UserData;

public class JsonValidator {
    public static boolean validateUserData(UserData data){
        return data.username() != null &&
                data.password() != null &&
                data.email() != null;
    }

    public static boolean validateLoginData(LoginRequest data){
        return data.username() != null &&
                data.password() != null;
    }
}
