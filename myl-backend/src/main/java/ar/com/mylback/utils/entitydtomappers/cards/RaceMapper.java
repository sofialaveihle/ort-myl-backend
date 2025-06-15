package ar.com.mylback.utils.entitydtomappers.cards;

import ar.com.mylback.dal.entities.cards.Race;
import ar.com.myldtos.cards.RaceDTO;
import jakarta.validation.constraints.NotNull;

public class RaceMapper extends CardPropertiesMapper<Race, RaceDTO> {
    @NotNull
    @Override
    public RaceDTO toDTO(Race race) {
        RaceDTO raceDTO = new RaceDTO();
        super.toDTO(race, raceDTO);
        return raceDTO;
    }

    @NotNull
    @Override
    public Race toEntity(RaceDTO raceDTO) {
        Race race = new Race();
        super.toEntity(raceDTO, race);
        return race;
    }
}
