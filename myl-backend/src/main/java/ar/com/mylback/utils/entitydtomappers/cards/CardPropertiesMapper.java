package ar.com.mylback.utils.entitydtomappers.cards;

import ar.com.mylback.dal.entities.cards.CardProperties;
import ar.com.myldtos.cards.CardPropertiesDTO;

import java.util.*;
import java.util.stream.Collectors;

public abstract class CardPropertiesMapper<T extends CardProperties, R extends CardPropertiesDTO> {
    public void toDTO(T cardProperties, R cardPropertiesDTO) {
        if (cardPropertiesDTO != null && cardProperties != null) {
            cardPropertiesDTO.setId(cardProperties.getId());
            cardPropertiesDTO.setName(cardProperties.getName());
        }
    }

    public abstract R toDTO(T cardProperties);

    public List<R> toDTO(Collection<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return new ArrayList<>();
        }
        return entities.stream()
                .map(this::toDTO)
                .toList();
    }

    public void toEntity(R cardPropertiesDTO, T cardProperties) {
        if (cardProperties != null && cardPropertiesDTO != null) {
            cardProperties.setId(cardPropertiesDTO.getId());
            cardProperties.setName(cardPropertiesDTO.getName());
        }
    }

    public abstract T toEntity(R cardPropertiesDTO);

    public Set<T> toEntities(Collection<R> DTOs) {
        if (DTOs == null || DTOs.isEmpty()) {
            return new HashSet<>();
        }
        return DTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toSet());
    }
}
