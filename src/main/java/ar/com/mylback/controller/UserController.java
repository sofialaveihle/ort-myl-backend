package ar.com.mylback.controller;

import ar.com.mylback.auth.FirebaseInitializer;
import ar.com.mylback.dal.crud.cards.DAO;
import ar.com.mylback.dal.entities.users.Player;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class UserController {

    private static final Gson gson = new Gson();

    public String registerUser(String requestBody) {
        FirebaseInitializer.init();

        UserRecord userRecord = null;

        try {
            JsonObject json = gson.fromJson(requestBody, JsonObject.class);
            String email = json.get("email").getAsString();
            String password = json.get("password").getAsString();

            // 1. Crear en Firebase
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password);

            userRecord = FirebaseAuth.getInstance().createUser(request);

            // 2. Insertar en base de datos
            Player user = new Player();
            user.setEmail(email);
            user.setUuid(userRecord.getUid());

            new DAO<>(Player.class).save(user);

            // TODO: Hay qeu crear un SMTP si queremos enviar
            //  desde el backend la notificación de verificación
            //   sino debe hacerlo el FRONT
            FirebaseAuth.getInstance().generateEmailVerificationLink(email);

            return "{\"message\": \"Usuario registrado correctamente\"}";

        } catch (Exception e) {
            //  Rollback si se creó el usuario en Firebase pero MySQL falló
            if (userRecord != null) {
                try {
                    FirebaseAuth.getInstance().deleteUser(userRecord.getUid());
                } catch (Exception deleteEx) {
                    System.err.println("Error al revertir usuario en Firebase: " + deleteEx.getMessage());
                }
            }

            System.err.println("Error al registrar usuario: " + e.getMessage());
            return "{\"error\": \"Error al registrar usuario: " + e.getMessage() + "\"}";
        }
    }
}
