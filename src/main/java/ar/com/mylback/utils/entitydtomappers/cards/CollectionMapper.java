package ar.com.mylback.utils.entitydtomappers.cards;

import ar.com.mylback.dal.entities.cards.Collection;
import ar.com.myldtos.cards.CollectionDTO;

public class CollectionMapper extends CardPropertiesMapper<Collection, CollectionDTO> {
    @Override
    public CollectionDTO toDTO(Collection collection) {
        CollectionDTO collectionDTO = new CollectionDTO();
        super.toDTO(collection, collectionDTO);
        return collectionDTO;
    }
}
