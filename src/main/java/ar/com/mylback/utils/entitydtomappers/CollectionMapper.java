package ar.com.mylback.utils.entitydtomappers;

import ar.com.mylback.dal.entities.cards.Collection;
import ar.com.myldtos.cards.CollectionDTO;

public class CollectionMapper {
    public static CollectionDTO toDTO(Collection collection) {
        CollectionDTO collectionDTO = new CollectionDTO();
        CardPropertiesMapper.toDTO(collection, collectionDTO);
        return collectionDTO;
    }
}
