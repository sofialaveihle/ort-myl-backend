package ar.com.mylback.controller;

import ar.com.mylback.dal.crud.cards.DAOCardProperties;
import ar.com.mylback.dal.entities.cards.KeyWord;
import ar.com.mylback.utils.MylException;
import ar.com.mylback.utils.entitydtomappers.cards.KeyWordMapper;
import ar.com.myldtos.cards.KeyWordDTO;
import com.google.gson.Gson;

public class KeyWordController extends CardPropertyController<KeyWord, KeyWordDTO, KeyWordMapper> {
    public KeyWordController(Gson gson, KeyWordMapper mapper, DAOCardProperties<KeyWord, Integer> daoKeyWord) throws MylException {
        super(gson, mapper, daoKeyWord);
    }
}
