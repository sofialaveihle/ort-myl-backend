package ar.com.mylback.utils.entitydtomappers;

import ar.com.mylback.dal.entities.cards.Card;
import ar.com.mylback.utils.ImageUrlGenerator;
import jakarta.validation.constraints.NotNull;
import ar.com.myldtos.cards.CardDTO;

public class CardMapper {
    @NotNull
    public static CardDTO toDTO(Card card) {
        if (card == null) return null;
        CardDTO cardDTO = new CardDTO();
        CardPropertiesMapper.toDTO(card, cardDTO);

        String imageUrl = ImageUrlGenerator.getInstance().generatePresignedUrl(card.getImageUuid());
        cardDTO.setImageUrl(imageUrl); // TODO
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
