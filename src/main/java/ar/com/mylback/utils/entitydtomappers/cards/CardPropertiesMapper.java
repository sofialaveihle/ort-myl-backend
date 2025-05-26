package ar.com.mylback.utils.entitydtomappers.cards;

import ar.com.mylback.dal.entities.cards.CardProperties;
import ar.com.myldtos.cards.CardPropertiesDTO;

public class CardPropertiesMapper {
    public static void toDTO(CardProperties cardProperties, CardPropertiesDTO cardPropertiesDTO) {
        if (cardPropertiesDTO != null && cardProperties != null) {
            cardPropertiesDTO.setId(cardProperties.getId());
            cardPropertiesDTO.setName(cardProperties.getName());
        }
    }

    public static CardPropertiesDTO toDTO(CardProperties cardProperties) {
        CardPropertiesDTO cardPropertiesDTO = new CardPropertiesDTO();
        if (cardProperties != null) {
            cardPropertiesDTO.setId(cardProperties.getId());
            cardPropertiesDTO.setName(cardProperties.getName());
        }
        return cardPropertiesDTO;
    }
}
