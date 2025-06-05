package ar.com.mylback.utils.entitydtomappers.users;

import ar.com.mylback.dal.entities.users.Store;
import users.StoreDTO;

public class StoreMapper extends UserMapper {
    public StoreDTO toDTO(Store store) {
        StoreDTO storeDTO = new StoreDTO();
        if (store != null) {
            super.toDTO(store, storeDTO);
            storeDTO.setUrl(store.getUrl());
            storeDTO.setPhoneNumber(store.getPhoneNumber());
            storeDTO.setAddress(store.getAddress());
            storeDTO.setValid(store.isValid());
        }
        return storeDTO;
    }

    public Store fromDTO(StoreDTO dto) {
        if (dto == null) return null;

        Store store = new Store();
        super.fromDTO(dto, store);
        store.setAddress(dto.getAddress());
        store.setPhoneNumber(dto.getPhoneNumber());
        store.setUrl(dto.getUrl());
        store.setValid(false);

        return store;
    }

    public void updateEntityFromDTO(StoreDTO dto, final Store store) {
        if (dto != null && store != null) {
            super.fromDTO(dto, store); // uuid, email, name
            store.setAddress(dto.getAddress());
            store.setPhoneNumber(dto.getPhoneNumber());
            store.setUrl(dto.getUrl());
        }
    }

}
