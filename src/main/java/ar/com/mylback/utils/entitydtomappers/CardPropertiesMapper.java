package ar.com.mylback.utils.entitydtomappers;

import ar.com.mylback.dal.entities.CardProperties;
import myldtos.cards.CardPropertiesDTO;

public class CardPropertiesMapper {
    public static void toDTO(CardProperties cardProperties, CardPropertiesDTO cardPropertiesDTO) {
        if (cardPropertiesDTO != null && cardProperties != null) {
            cardPropertiesDTO.setId(cardProperties.getId());
            cardPropertiesDTO.setName(cardProperties.getName());
        }
    }
}
