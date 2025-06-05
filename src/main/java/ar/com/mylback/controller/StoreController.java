package ar.com.mylback.controller;

import ar.com.mylback.auth.FirebaseAuthValidator;
import ar.com.mylback.auth.FirebaseInitializer;
import ar.com.mylback.dal.crud.users.DAOStore;
import ar.com.mylback.dal.entities.users.Store;
import ar.com.mylback.utils.MylException;
import ar.com.mylback.utils.QueryString;
import ar.com.mylback.utils.entitydtomappers.users.StoreMapper;
import com.google.gson.Gson;
import users.StoreDTO;

import java.util.List;

public class StoreController {
    private final Gson gson;
    private final DAOStore daoStore;
    private final StoreMapper storeMapper;

    public StoreController(Gson gson, DAOStore daoStore, StoreMapper storeMapper) throws MylException {
        if (gson != null && daoStore != null && storeMapper != null) {
            this.gson = gson;
            this.daoStore = daoStore;
            this.storeMapper = storeMapper;
        } else {
            throw new MylException(MylException.Type.NULL_PARAMETER);
        }
    }

    private boolean isAdmin(String uid) {
        // TODO: cambiar esto por una verificación real
        return uid.equals("admin-uid-temporal");
    }

    public String getStoresByValidation(String authHeader, QueryString queryString) throws Exception {
        // Solo verificamos el token si se están pidiendo tiendas NO validadas
        boolean isValid = queryString.getValid();
        if (!isValid) {
            String uid = FirebaseAuthValidator.validateAndGetUid(authHeader);

            // TODO: validar que el uid corresponda a un usuario con rol admin
            // Por ahora tiramos excepción si no hay validación de rol
            if (!isAdmin(uid)) {
                throw new Exception("Acceso no autorizado. Solo administradores pueden ver tiendas no validadas.");
            }
        }

        List<Store> stores = daoStore.findAllByValidationStatus(isValid);

        List<StoreDTO> storeDTOs = stores.stream()
                .map(storeMapper::toDTO)
                .toList();

        return gson.toJson(storeDTOs);
    }

    public String getStoreByUuid(String uuid, String authHeader) throws Exception {
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

        StoreDTO dto = storeMapper.toDTO(store);
        return new Gson().toJson(dto);
    }

    public String validateStore(String uuid, String authHeader) throws Exception {
        String uid = FirebaseAuthValidator.validateAndGetUid(authHeader);

        if (!isAdmin(uid)) {
            throw new Exception("Acceso no autorizado. Solo administradores pueden validar tiendas.");
        }

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

        Store store = daoStore.findByUuid(uid);

        if (store == null) {
            return "{\"error\": \"Tienda no encontrada\"}";
        }

        storeMapper.updateEntityFromDTO(dto, store);
        daoStore.update(store);

        return "{\"message\": \"Tienda actualizada correctamente\"}";
    }
}

