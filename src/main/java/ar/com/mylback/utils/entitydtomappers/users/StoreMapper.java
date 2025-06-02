package ar.com.mylback.utils.entitydtomappers.users;

import ar.com.mylback.dal.entities.users.Store;
import users.StoreDTO;

public class StoreMapper {
    public static StoreDTO toDTO(Store store) {
        StoreDTO storeDTO = new StoreDTO();
        if (store != null) {
            UserMapper.toDTO(store, storeDTO);
            storeDTO.setUrl(store.getUrl());
            storeDTO.setPhoneNumber(store.getPhoneNumber());
            storeDTO.setAddress(store.getAddress());
            storeDTO.setValid(store.isValid());
        }
        return storeDTO;
    }

    public static Store fromDTO(StoreDTO dto) {
        if (dto == null) return null;

        Store store = new Store();
        UserMapper.fromDTO(dto, store);
        store.setAddress(dto.getAddress());
        store.setPhoneNumber(dto.getPhoneNumber());
        store.setUrl(dto.getUrl());
        store.setValid(false);

        return store;
    }

    public static void updateEntityFromDTO(StoreDTO dto, Store store) {
        if (dto != null && store != null) {
            UserMapper.fromDTO(dto, store); // uuid, email, name
            store.setAddress(dto.getAddress());
            store.setPhoneNumber(dto.getPhoneNumber());
            store.setUrl(dto.getUrl());
        }
    }

}
