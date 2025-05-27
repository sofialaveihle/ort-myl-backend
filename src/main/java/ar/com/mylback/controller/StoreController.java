package ar.com.mylback.controller;

import ar.com.mylback.auth.FirebaseInitializer;
import ar.com.mylback.dal.crud.cards.DAO;
import ar.com.mylback.dal.entities.users.Store;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class StoreController {

    private static final Gson gson = new Gson();

    public String registerStore(String requestBody) {
        FirebaseInitializer.init();

        UserRecord userRecord = null;

        try {
            JsonObject json = gson.fromJson(requestBody, JsonObject.class);
            String email = json.get("email").getAsString();
            String password = json.get("password").getAsString();
            String storeName = json.get("storeName").getAsString();
            String address = json.get("address").getAsString();
            String phone = json.get("phone").getAsString();


            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password);

            userRecord = FirebaseAuth.getInstance().createUser(request);


            Store store = new Store();
            store.setEmail(email);
            store.setUuid(userRecord.getUid());
            store.setName(storeName);
            store.setAddress(address);
            store.setPhoneNumber(phone);
            store.setValid(false);

            new DAO<>(Store.class).save(store);

            // TODO: Hay qeu crear un SMTP si queremos enviar
            //  desde el backend la notificación de verificación
            //   sino debe hacerlo el FRONT
            FirebaseAuth.getInstance().generateEmailVerificationLink(email);

            return "{\"message\": \"Tienda registrada correctamente\"}";

        } catch (Exception e) {
            // ROLLBACK Si Firebase ya creó el usuario y falla se elimina
            if (userRecord != null) {
                try {
                    FirebaseAuth.getInstance().deleteUser(userRecord.getUid());
                } catch (Exception deleteEx) {
                    System.err.println("Error al revertir usuario en Firebase: " + deleteEx.getMessage());
                }
            }

            System.err.println("Error al registrar tienda: " + e.getMessage());
            return "{\"error\": \"Error al registrar tienda: " + e.getMessage() + "\"}";
        }
    }
}
