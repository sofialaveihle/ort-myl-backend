package ar.com.mylback.utils.entitydtomappers.cards;

import ar.com.mylback.dal.entities.cards.CardProperties;
import ar.com.myldtos.cards.CardPropertiesDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class CardPropertiesMapper<T extends CardProperties, R extends CardPropertiesDTO> {
    public void toDTO(T cardProperties, R cardPropertiesDTO) {
        if (cardPropertiesDTO != null && cardProperties != null) {
            cardPropertiesDTO.setId(cardProperties.getId());
            cardPropertiesDTO.setName(cardProperties.getName());
        }
    }

    public abstract R toDTO(T cardProperties);

    public List<R> toDTO(Set<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return new ArrayList<>();
        }
        return entities.stream()
                .map(this::toDTO)
                .toList();
    }
}
