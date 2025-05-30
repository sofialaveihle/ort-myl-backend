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

    public String getAllValidStores() {
        try {
            DAOStore daoStore = new DAOStore();
            List<Store> stores = daoStore.findAllValid();

            List<StoreDTO> storeDTOs = stores.stream()
                    .map(StoreMapper::toDTO)
                    .toList();

            return new Gson().toJson(storeDTOs);

        } catch (Exception e) {
            System.err.println("Error al obtener tiendas: " + e.getMessage());
            return "{\"error\": \"No se pudieron obtener las tiendas\"}";
        }
    }

    public String getStoreByUuid(String uuid) {
        try {
            DAOStore daoStore = new DAOStore();
            Store store = daoStore.findByUuid(uuid);

            if (store == null || !store.isValid()) {
                return "{\"error\": \"Tienda no encontrada o no validada\"}";
            }

            StoreDTO dto = StoreMapper.toDTO(store);
            return new Gson().toJson(dto);

        } catch (Exception e) {
            System.err.println("Error al obtener tienda por UUID: " + e.getMessage());
            return "{\"error\": \"No se pudo obtener la tienda\"}";
        }
    }


    public String getAllUnverifiedStores(String authHeader) {
        try {
            String uid = FirebaseAuthValidator.validateAndGetUid(authHeader);

            // TODO: validar que uid sea admin
            DAOStore daoStore = new DAOStore();
            List<Store> unverifiedStores = daoStore.findAllUnverified();

            List<StoreDTO> storeDTOs = unverifiedStores.stream()
                    .map(StoreMapper::toDTO)
                    .toList();

            return gson.toJson(storeDTOs);
        } catch (Exception e) {
            System.err.println("Error al obtener tiendas no verificadas: " + e.getMessage());
            return "{\"error\": \"No se pudieron obtener las tiendas no verificadas\"}";
        }
    }

    public String getUnverifiedStoreByUuid(String uuid, String authHeader) {
        try {
            String uid = FirebaseAuthValidator.validateAndGetUid(authHeader);
            // TODO: validar que uid sea admin

            DAOStore daoStore = new DAOStore();
            Store store = daoStore.findByUuid(uuid);

            if (store == null || store.isValid()) {
                return "{\"error\": \"Tienda no encontrada o ya validada\"}";
            }

            StoreDTO dto = StoreMapper.toDTO(store);
            return gson.toJson(dto);

        } catch (Exception e) {
            System.err.println("Error al obtener tienda no verificada por UUID: " + e.getMessage());
            return "{\"error\": \"No se pudo obtener la tienda\"}";
        }
    }

    public String validateStore(String uuid, String authHeader) {
        try {
            String uid = FirebaseAuthValidator.validateAndGetUid(authHeader);
            // TODO: validar que uid sea admin

            DAOStore daoStore = new DAOStore();
            Store store = daoStore.findByUuid(uuid);

            if (store == null) {
                return "{\"error\": \"Tienda no encontrada\"}";
            }

            store.setValid(true);
            daoStore.update(store);

            return "{\"message\": \"Tienda validada correctamente\"}";
        } catch (Exception e) {
            System.err.println("Error al validar tienda: " + e.getMessage());
            return "{\"error\": \"No se pudo validar la tienda\"}";
        }
    }

    public String updateStore(String requestBody, String authHeader) {
        FirebaseInitializer.init();

        try {
            String uid = FirebaseAuthValidator.validateAndGetUid(authHeader);

            StoreDTO dto = gson.fromJson(requestBody, StoreDTO.class);

            DAOStore daoStore = new DAOStore();
            Store store = daoStore.findByUuid(uid);

            if (store == null) {
                return "{\"error\": \"Tienda no encontrada\"}";
            }

            store.setName(dto.getName());
            store.setAddress(dto.getAddress());
            store.setPhoneNumber(dto.getPhoneNumber());
            store.setUrl(dto.getUrl());

            daoStore.update(store);

            return "{\"message\": \"Tienda actualizada correctamente\"}";

        } catch (Exception e) {
            System.err.println("Error al actualizar tienda: " + e.getMessage());
            return "{\"error\": \"Error al actualizar tienda: " + e.getMessage() + "\"}";
        }
    }



}
