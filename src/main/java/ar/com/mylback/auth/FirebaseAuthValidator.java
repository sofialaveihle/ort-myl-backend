package ar.com.mylback.auth;

import com.google.firebase.auth.FirebaseToken;

public class FirebaseAuthValidator {
    private static FirebaseToken getVerifiedToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Falta el token o tiene un formato incorrecto");
        }

        String idToken = authHeader.substring("Bearer ".length());

        return FirebaseAuthUtils.verifyIdToken(idToken);
    }

    public static String validateAndGetEmail(String authHeader) {
        return getVerifiedToken(authHeader).getEmail();
    }

    public static String validateAndGetUid(String authHeader) {
        return getVerifiedToken(authHeader).getUid();
    }

    // Esta permite obtener el token completo, que contiene a la vez el uid y el email
    public static FirebaseToken validateAndGetFullToken(String authHeader) {
        return getVerifiedToken(authHeader);
    }
}


