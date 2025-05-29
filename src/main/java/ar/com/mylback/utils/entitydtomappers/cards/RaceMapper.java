package ar.com.mylback.utils.entitydtomappers.cards;

import ar.com.mylback.dal.entities.cards.Race;
import ar.com.myldtos.cards.RaceDTO;

public class RaceMapper extends CardPropertiesMapper<Race, RaceDTO> {
    @Override
    public RaceDTO toDTO(Race race) {
        RaceDTO raceDTO = new RaceDTO();
        super.toDTO(race, raceDTO);
        return raceDTO;
    }
}
