package ar.com.mylback.utils.entitydtomappers.cards;

import ar.com.mylback.dal.entities.cards.Rarity;
import ar.com.myldtos.cards.RarityDTO;

public class RarityMapper extends CardPropertiesMapper<Rarity, RarityDTO> {
    @Override
    public RarityDTO toDTO(Rarity rarity) {
        RarityDTO rarityDTO = new RarityDTO();
        super.toDTO(rarity, rarityDTO);
        return rarityDTO;
    }
}
