package ar.com.mylback.utils.entitydtomappers;

import ar.com.mylback.dal.entities.Race;
import ar.com.myldtos.cards.RaceDTO;

public class RaceMapper {
    public static RaceDTO toDTO(Race race) {
        RaceDTO raceDTO = new RaceDTO();
        CardPropertiesMapper.toDTO(race, raceDTO);
        return raceDTO;
    }
}
