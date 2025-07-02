package ar.com.mylback.controller;

import ar.com.mylback.dal.crud.cards.DAOCardProperties;
import ar.com.mylback.dal.entities.cards.Race;
import ar.com.mylback.utils.MylException;
import ar.com.mylback.utils.entitydtomappers.cards.RaceMapper;
import ar.com.myldtos.cards.RaceDTO;
import com.google.gson.Gson;

public class RaceController extends CardPropertyController<Race, RaceDTO, RaceMapper> {
    public RaceController(Gson gson, RaceMapper mapper, DAOCardProperties<Race, Integer> daoRace) throws MylException {
        super(gson, mapper, daoRace);
    }
}
