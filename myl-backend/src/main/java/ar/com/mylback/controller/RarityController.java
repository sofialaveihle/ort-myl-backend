package ar.com.mylback.controller;

import ar.com.mylback.dal.crud.cards.DAOCardProperties;
import ar.com.mylback.dal.entities.cards.Rarity;
import ar.com.mylback.utils.MylException;
import ar.com.mylback.utils.entitydtomappers.cards.RarityMapper;
import ar.com.myldtos.cards.RarityDTO;
import com.google.gson.Gson;

public class RarityController extends CardPropertyController<Rarity, RarityDTO, RarityMapper> {
    public RarityController(Gson gson, RarityMapper mapper, DAOCardProperties<Rarity, Integer> daoRarity) throws MylException {
        super(gson, mapper, daoRarity);
    }
}
