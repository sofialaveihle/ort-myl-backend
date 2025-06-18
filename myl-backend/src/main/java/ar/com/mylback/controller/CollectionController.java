package ar.com.mylback.controller;

import ar.com.mylback.dal.entities.cards.Collection;
import ar.com.mylback.utils.MylException;
import ar.com.mylback.utils.entitydtomappers.cards.CollectionMapper;
import ar.com.myldtos.cards.CollectionDTO;
import com.google.gson.Gson;

public class CollectionController extends CardPropertyController<Collection, CollectionDTO, CollectionMapper> {
    public CollectionController(Gson gson, CollectionMapper mapper) throws MylException {
        super(gson, mapper, Collection.class);
    }
}
