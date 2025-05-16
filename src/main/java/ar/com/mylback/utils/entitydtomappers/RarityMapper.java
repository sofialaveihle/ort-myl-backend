package ar.com.mylback.utils.entitydtomappers;

import ar.com.mylback.dal.entities.Rarity;
import myldtos.cards.RarityDTO;

public class RarityMapper {
    public static RarityDTO toDTO(Rarity rarity) {
        RarityDTO rarityDTO = new RarityDTO();
        CardPropertiesMapper.toDTO(rarity, rarityDTO);
        return rarityDTO;
    }
}
