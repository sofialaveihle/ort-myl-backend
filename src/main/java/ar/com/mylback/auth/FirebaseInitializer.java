package ar.com.mylback.auth;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.InputStream;

public class FirebaseInitializer {

    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;

        try {
            // 1. Leer el archivo .json de la cuenta de servicio
            InputStream serviceAccount = FirebaseInitializer.class
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

            initialized = true;
            System.out.println("[Firebase] Inicializado correctamente.");

        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar Firebase", e);
        }
    }
}