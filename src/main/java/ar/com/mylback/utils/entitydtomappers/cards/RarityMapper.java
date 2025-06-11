package ar.com.mylback.utils.entitydtomappers.cards;

import ar.com.mylback.dal.entities.cards.Rarity;
import ar.com.myldtos.cards.RarityDTO;
import jakarta.validation.constraints.NotNull;

public class RarityMapper extends CardPropertiesMapper<Rarity, RarityDTO> {
    @NotNull
    @Override
    public RarityDTO toDTO(Rarity rarity) {
        RarityDTO rarityDTO = new RarityDTO();
        super.toDTO(rarity, rarityDTO);
        return rarityDTO;
    }

    @NotNull
    @Override
    public Rarity toEntity(RarityDTO rarityDTO) {
        Rarity rarity = new Rarity();
        super.toEntity(rarityDTO, rarity);
        return rarity;
    }
}
