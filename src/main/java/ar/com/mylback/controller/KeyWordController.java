package ar.com.mylback.controller;

import ar.com.mylback.dal.entities.cards.KeyWord;
import ar.com.mylback.utils.entitydtomappers.cards.KeyWordMapper;
import ar.com.myldtos.cards.KeyWordDTO;

public class KeyWordController extends CardPropertyController<KeyWord, KeyWordDTO, KeyWordMapper> {
    public KeyWordController(KeyWordMapper mapper) {
        super(mapper, KeyWord.class);
    }
}
