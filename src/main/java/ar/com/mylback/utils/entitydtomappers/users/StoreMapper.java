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
}
