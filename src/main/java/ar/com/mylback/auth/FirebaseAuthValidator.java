package ar.com.mylback.auth;

import com.google.firebase.auth.FirebaseToken;

public class FirebaseAuthValidator {

    private static FirebaseToken getVerifiedToken(String authHeader) throws Exception {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new Exception("Falta el token o tiene un formato incorrecto");
        }

        String idToken = authHeader.substring("Bearer ".length());
        return FirebaseAuthUtils.verifyIdToken(idToken);
    }

    public static String validateAndGetEmail(String authHeader) throws Exception {
        return getVerifiedToken(authHeader).getEmail();
    }

    public static String validateAndGetUid(String authHeader) throws Exception {
        return getVerifiedToken(authHeader).getUid();
    }

    public static FirebaseToken validateAndGetFullToken(String authHeader) throws Exception {
        return getVerifiedToken(authHeader);
    }
}


