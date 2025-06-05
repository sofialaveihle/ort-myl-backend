package ar.com.mylback.controller;

import ar.com.mylback.dal.entities.cards.Rarity;
import ar.com.mylback.utils.entitydtomappers.cards.RarityMapper;
import ar.com.myldtos.cards.RarityDTO;

public class RarityController extends CardPropertyController<Rarity, RarityDTO, RarityMapper> {
    public RarityController(RarityMapper mapper) {
        super(mapper, Rarity.class);
    }
}
