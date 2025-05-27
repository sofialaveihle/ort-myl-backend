package ar.com.mylback.utils.entitydtomappers.cards;

import ar.com.mylback.dal.entities.cards.Rarity;
import ar.com.myldtos.cards.RarityDTO;

public class RarityMapper {
    public static RarityDTO toDTO(Rarity rarity) {
        RarityDTO rarityDTO = new RarityDTO();
        CardPropertiesMapper.toDTO(rarity, rarityDTO);
        return rarityDTO;
    }
}
