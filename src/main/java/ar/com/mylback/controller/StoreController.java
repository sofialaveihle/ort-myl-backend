package ar.com.mylback.controller;

import ar.com.mylback.auth.FirebaseAuthValidator;
import ar.com.mylback.auth.FirebaseInitializer;
import ar.com.mylback.dal.crud.cards.DAO;
import ar.com.mylback.dal.crud.users.DAOStore;
import ar.com.mylback.dal.entities.users.Store;
import ar.com.mylback.utils.MylException;
import ar.com.mylback.utils.entitydtomappers.users.StoreMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import users.StoreDTO;
import users.StoreRegisterDTO;

import java.util.List;

public class StoreController {

    private static final Gson gson = new Gson();

    private boolean isAdmin(String uid) {
        // TODO: cambiar esto por una verificación real
        return uid.equals("admin-uid-temporal");
    }

    public String getStoresByValidation(String authHeader, boolean isValid) throws Exception {
        // Solo verificamos el token si se están pidiendo tiendas NO validadas
        if (!isValid) {
            String uid = FirebaseAuthValidator.validateAndGetUid(authHeader);

            // TODO: validar que el uid corresponda a un usuario con rol admin
            // Por ahora tiramos excepción si no hay validación de rol
            if (!isAdmin(uid)) {
                throw new Exception("Acceso no autorizado. Solo administradores pueden ver tiendas no validadas.");
            }
        }

        DAOStore daoStore = new DAOStore();
        List<Store> stores = daoStore.findAllByValidationStatus(isValid);

        List<StoreDTO> storeDTOs = stores.stream()
                .map(StoreMapper::toDTO)
                .toList();

        return gson.toJson(storeDTOs);
    }

    public String getStoreByUuid(String uuid, String authHeader) throws Exception {
        DAOStore daoStore = new DAOStore();
        Store store = daoStore.findByUuid(uuid);

        if (store == null) {
            throw new Exception("Tienda no encontrada");
        }

        if (!store.isValid()) {
            String uid = FirebaseAuthValidator.validateAndGetUid(authHeader);
            if (!isAdmin(uid)) {
                throw new Exception("Acceso no autorizado a tienda no validada");
            }
        }

        StoreDTO dto = StoreMapper.toDTO(store);
        return new Gson().toJson(dto);
    }

    public String validateStore(String uuid, String authHeader) throws Exception {
        String uid = FirebaseAuthValidator.validateAndGetUid(authHeader);

        if (!isAdmin(uid)) {
            throw new Exception("Acceso no autorizado. Solo administradores pueden validar tiendas.");
        }

        DAOStore daoStore = new DAOStore();
        Store store = daoStore.findByUuid(uuid);

        if (store == null) {
            throw new Exception("Tienda no encontrada");
        }

        store.setValid(true);
        daoStore.update(store);

        return "{\"message\": \"Tienda validada correctamente\"}";
    }

    public String updateStore(String requestBody, String authHeader) throws Exception {
        FirebaseInitializer.init();

        String uid = FirebaseAuthValidator.validateAndGetUid(authHeader);
        StoreDTO dto = gson.fromJson(requestBody, StoreDTO.class);

        DAOStore daoStore = new DAOStore();
        Store store = daoStore.findByUuid(uid);

        if (store == null) {
            return "{\"error\": \"Tienda no encontrada\"}";
        }

        StoreMapper.updateEntityFromDTO(dto, store);
        daoStore.update(store);

        return "{\"message\": \"Tienda actualizada correctamente\"}";
    }
}

