package ar.com.mylback.auth;

import ar.com.mylback.utils.MylException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;

import java.io.IOException;
import java.io.InputStream;

public class FirebaseAuthValidator {

    public FirebaseAuthValidator() throws MylException {
        init();
    }

    private void init() throws MylException {
        try {
            // 1. Leer el archivo .json de la cuenta de servicio
            InputStream serviceAccount = FirebaseAuthValidator.class
                    .getClassLoader()
                    .getResourceAsStream("firebase/serviceAccountKey.json");

            if (serviceAccount == null) {
                throw new RuntimeException("No se encontró el archivo serviceAccountKey.json");
            }

            // 2. Configurar las credenciales con el stream del archivo
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            // 3. Inicializar FirebaseApp si aún no existe
            FirebaseApp.initializeApp(options);

            System.out.println("[Firebase] Initialized.");
        } catch (IOException e) {
            throw new MylException();
        }
    }

    private FirebaseToken getVerifiedToken(String authHeader) throws MylException {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new MylException(MylException.Type.NULL_PARAMETER, "null or empty token");
        }

        try {
            String idToken = authHeader.substring("Bearer ".length());
            return FirebaseAuth.getInstance().verifyIdToken(idToken);
        } catch (FirebaseAuthException e) {
            throw new MylException(MylException.Type.FIREBASE_ERROR, e.getMessage());
        }
    }

    public String validateAndGetEmail(String authHeader) throws MylException {
        return getVerifiedToken(authHeader).getEmail();
    }

    public String validateAndGetUid(String authHeader) throws MylException {
        return getVerifiedToken(authHeader).getUid();
    }

    public FirebaseToken validateAndGetFullToken(String authHeader) throws MylException {
        return getVerifiedToken(authHeader);
    }

    public void deleteUser(String uid) throws MylException {
        try {
            FirebaseAuth.getInstance().deleteUser(uid);
        } catch (FirebaseAuthException e) {
            throw new MylException(MylException.Type.FIREBASE_ERROR, e.getMessage());
        }
    }

    public UserRecord getUserByUID(String uui) throws MylException {
        try {
            return FirebaseAuth.getInstance().getUser(uui);
        } catch (FirebaseAuthException e) {
            throw new MylException(MylException.Type.FIREBASE_ERROR, e.getMessage());
        }
    }
}


