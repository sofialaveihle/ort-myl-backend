package ar.com.mylback.controller;

import ar.com.mylback.dal.entities.cards.Race;
import ar.com.mylback.utils.entitydtomappers.cards.RaceMapper;
import ar.com.myldtos.cards.RaceDTO;

public class RaceController extends CardPropertyController<Race, RaceDTO, RaceMapper> {
    public RaceController(RaceMapper mapper) {
        super(mapper, Race.class);
    }
}
