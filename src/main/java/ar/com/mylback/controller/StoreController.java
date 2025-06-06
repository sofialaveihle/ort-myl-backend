package ar.com.mylback.controller;

import ar.com.mylback.auth.FirebaseAuthValidator;
import ar.com.mylback.dal.crud.users.DAOStore;
import ar.com.mylback.dal.entities.users.Store;
import ar.com.mylback.utils.HttpResponse;
import ar.com.mylback.utils.MylException;
import ar.com.mylback.utils.url.QueryString;
import ar.com.mylback.utils.entitydtomappers.users.StoreMapper;
import ar.com.myldtos.ErrorTemplateDTO;
import ar.com.myldtos.SuccessTemplateDTO;
import com.google.gson.Gson;
import ar.com.myldtos.users.StoreDTO;

import java.util.List;

public class StoreController {
    private final Gson gson;
    private final FirebaseAuthValidator firebaseAuthValidator;
    private final DAOStore daoStore;
    private final StoreMapper storeMapper;

    public StoreController(Gson gson, FirebaseAuthValidator firebaseAuthValidator, DAOStore daoStore, StoreMapper storeMapper) throws MylException {
        if (gson != null && firebaseAuthValidator != null && daoStore != null && storeMapper != null) {
            this.gson = gson;
            this.firebaseAuthValidator = firebaseAuthValidator;
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

    public HttpResponse getStoresByValidation(String authHeader, QueryString queryString) {
        try {
            // Solo verificamos el token si se están pidiendo tiendas NO validadas
            boolean isValid = queryString.getValid();
            if (!isValid) {
                String uid = firebaseAuthValidator.validateAndGetUid(authHeader);

                if (!isAdmin(uid)) {
                    return new HttpResponse(401, gson.toJson(new ErrorTemplateDTO(401, "Acceso no autorizado. Solo administradores pueden ver tiendas no validadas.")));
                }
            }

            List<Store> stores = daoStore.findAllByValidationStatus(isValid);

            List<StoreDTO> storeDTOs = stores.stream()
                    .map(storeMapper::toDTO)
                    .toList();

            return new HttpResponse(200, gson.toJson(storeDTOs));
        } catch (MylException e) {
            return new HttpResponse(400, gson.toJson(new ErrorTemplateDTO(400, "Error de negocio", e.getMessage())));
        }
    }

    public HttpResponse getStoreByUuid(String uuid, String authHeader) {
        try {
            Store store = daoStore.findByUuid(uuid);

            if (store == null) {
                return new HttpResponse(404, gson.toJson(new ErrorTemplateDTO(404, "Tienda no encontrada")));
            }

            if (!store.isValid()) {
                String uid = firebaseAuthValidator.validateAndGetUid(authHeader);
                if (!isAdmin(uid)) {
                    return new HttpResponse(403, gson.toJson(new ErrorTemplateDTO(403, "Acceso no autorizado a tienda no validada")));
                }
            }

            StoreDTO dto = storeMapper.toDTO(store);
            return new HttpResponse(200, gson.toJson(dto));
        } catch (MylException e) {
            return new HttpResponse(400, gson.toJson(new ErrorTemplateDTO(400, "Error de negocio", e.getMessage())));
        }
    }

    public HttpResponse validateStore(String uuid, String authHeader) {
        try {
            String uid = firebaseAuthValidator.validateAndGetUid(authHeader);

            if (!isAdmin(uid)) {
                return new HttpResponse(403, gson.toJson(new ErrorTemplateDTO(403, "Acceso no autorizado. Solo administradores pueden validar tiendas.")));
            }

            Store store = daoStore.findByUuid(uuid);

            if (store == null) {
                return new HttpResponse(404, gson.toJson(new ErrorTemplateDTO(404, "Tienda no encontrada")));
            }

            store.setValid(true);
            daoStore.update(store);

            return new HttpResponse(200, gson.toJson(new SuccessTemplateDTO("Tienda validada correctamente")));
        } catch (MylException e) {
            return new HttpResponse(400, gson.toJson(new ErrorTemplateDTO(400, "Error de negocio", e.getMessage())));
        }
    }

    public HttpResponse updateStore(String requestBody, String authHeader) {
        try {
            String uid = firebaseAuthValidator.validateAndGetUid(authHeader);
            StoreDTO dto = gson.fromJson(requestBody, StoreDTO.class);

            Store store = daoStore.findByUuid(uid);

            if (store == null) {
                return new HttpResponse(404, gson.toJson(new ErrorTemplateDTO(404, "Tienda no encontrada")));
            }

            storeMapper.updateEntityFromDTO(dto, store);
            daoStore.update(store);

            return new HttpResponse(200, gson.toJson(new SuccessTemplateDTO("Tienda actualizada correctamente")));
        } catch (MylException e) {
            return new HttpResponse(400, gson.toJson(new ErrorTemplateDTO(400, "Error de negocio", e.getMessage())));
        }
    }
}

