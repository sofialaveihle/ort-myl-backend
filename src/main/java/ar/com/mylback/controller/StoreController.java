package ar.com.mylback.controller;

import ar.com.mylback.auth.FirebaseInitializer;
import ar.com.mylback.dal.crud.DAO;
import ar.com.mylback.dal.entities.Store;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class StoreController {

    private static final Gson gson = new Gson();

    public String registerStore(String requestBody) throws Exception {
        FirebaseInitializer.init();

        JsonObject json = gson.fromJson(requestBody, JsonObject.class);
        String email = json.get("email").getAsString();
        String password = json.get("password").getAsString();
        String storeName = json.get("storeName").getAsString();
        String address = json.get("address").getAsString();
        String phone = json.get("phone").getAsString();

        // Crear usuario en Firebase
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);
        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

        // Crear tienda en base de datos
        Store store = new Store();
        store.setEmail(email);
        store.setFirebaseUid(userRecord.getUid());
        store.setStoreName(storeName);
        store.setAddress(address);
        store.setPhone(phone);

        new DAO<>(Store.class).save(store);

        FirebaseAuth.getInstance().generateEmailVerificationLink(email);

        return "{\"message\": \"Tienda registrada correctamente\"}";
    }
}
