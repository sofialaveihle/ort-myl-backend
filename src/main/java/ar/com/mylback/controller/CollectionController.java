package ar.com.mylback.controller;

import ar.com.mylback.dal.entities.cards.Collection;
import ar.com.mylback.utils.entitydtomappers.cards.CollectionMapper;
import ar.com.myldtos.cards.CollectionDTO;

public class CollectionController extends CardPropertyController<Collection, CollectionDTO, CollectionMapper> {
    public CollectionController(CollectionMapper mapper) {
        super(mapper, Collection.class);
    }
}
