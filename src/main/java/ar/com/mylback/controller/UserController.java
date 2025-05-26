package ar.com.mylback.controller;

import ar.com.mylback.auth.FirebaseInitializer;
import ar.com.mylback.dal.crud.DAO;
import ar.com.mylback.dal.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class UserController {

    private static final Gson gson = new Gson();

    public String registerUser(String requestBody) throws Exception {
        FirebaseInitializer.init();

        JsonObject json = gson.fromJson(requestBody, JsonObject.class);
        String email = json.get("email").getAsString();
        String password = json.get("password").getAsString();

        // Crear usuario en Firebase
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);
        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

        // Crear en base de datos
        User user = new User();
        user.setEmail(email);
        user.setFirebaseUid(userRecord.getUid());

        new DAO<>(User.class).save(user);

        // Enviar email de verificación
        FirebaseAuth.getInstance().generateEmailVerificationLink(email);

        return "{\"message\": \"Usuario registrado correctamente\"}";
    }
}
