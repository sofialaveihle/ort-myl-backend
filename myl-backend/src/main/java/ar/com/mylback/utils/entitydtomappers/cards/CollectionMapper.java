package ar.com.mylback.utils.entitydtomappers.cards;

import ar.com.mylback.dal.entities.cards.Collection;
import ar.com.myldtos.cards.CollectionDTO;
import jakarta.validation.constraints.NotNull;

public class CollectionMapper extends CardPropertiesMapper<Collection, CollectionDTO> {
    @NotNull
    @Override
    public CollectionDTO toDTO(Collection collection) {
        CollectionDTO collectionDTO = new CollectionDTO();
        super.toDTO(collection, collectionDTO);
        return collectionDTO;
    }

    @NotNull
    @Override
    public Collection toEntity(CollectionDTO collectionDTO) {
        Collection collection = new Collection();
        super.toEntity(collectionDTO, collection);
        return collection;
    }
}
