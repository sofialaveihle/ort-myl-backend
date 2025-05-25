package ar.com.mylback.utils.entitydtomappers;

import ar.com.mylback.dal.entities.Card;
import jakarta.validation.constraints.NotNull;
import myldtos.cards.CardDTO;

public class CardMapper {
    @NotNull
    public static CardDTO toDTO(Card card) {
        if (card == null) return null;
        CardDTO cardDTO = new CardDTO();
        CardPropertiesMapper.toDTO(card, cardDTO);
        cardDTO.setImageUrl("images/" + card.getImageUuid().toString() + ".webp"); // TODO
        cardDTO.setDescription(card.getDescription());
        cardDTO.setCost(card.getCost());
        cardDTO.setDamage(card.getDamage());
        cardDTO.setCollection(CollectionMapper.toDTO(card.getCollection()));
        cardDTO.setRarity(RarityMapper.toDTO(card.getRarity()));
        cardDTO.setType(TypeMapper.toDTO(card.getType()));
        cardDTO.setRace(RaceMapper.toDTO(card.getRace()));
        cardDTO.addFormats(FormatMapper.toDTO(card.getFormats()));
        cardDTO.addKeyWords(KeyWordMapper.toDTO(card.getKeyWords()));
        return cardDTO;
    }
}
