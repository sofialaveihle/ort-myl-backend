package ar.com.mylback.utils.entitydtomappers.cards;

import ar.com.mylback.dal.entities.cards.Card;
import ar.com.mylback.utils.ImageUrlGenerator;
import ar.com.mylback.utils.MylException;
import ar.com.myldtos.cards.CardDTO;
import jakarta.validation.constraints.NotNull;

public class CardMapper extends CardPropertiesMapper<Card, CardDTO> {
    private final ImageUrlGenerator imageUrlGenerator;
    private final CollectionMapper collectionMapper;
    private final RarityMapper rarityMapper;
    private final TypeMapper typeMapper;
    private final RaceMapper raceMapper;
    private final FormatMapper formatMapper;
    private final KeyWordMapper keyWordMapper;

    public CardMapper(ImageUrlGenerator imageUrlGenerator) throws MylException {
        if (imageUrlGenerator == null) {
            throw new MylException(MylException.Type.NULL_PARAMETER);
        }
        this.imageUrlGenerator = imageUrlGenerator;

        this.collectionMapper = new CollectionMapper();
        this.rarityMapper = new RarityMapper();
        this.typeMapper = new TypeMapper();
        this.raceMapper = new RaceMapper();
        this.formatMapper = new FormatMapper();
        this.keyWordMapper = new KeyWordMapper();
    }

    public CardMapper(
            ImageUrlGenerator imageUrlGenerator,
            CollectionMapper collectionMapper,
            RarityMapper rarityMapper,
            TypeMapper typeMapper,
            RaceMapper raceMapper,
            FormatMapper formatMapper,
            KeyWordMapper keyWordMapper
    ) throws MylException {
        if (imageUrlGenerator == null
                || collectionMapper == null
                || rarityMapper == null
                || typeMapper == null
                || raceMapper == null
                || formatMapper == null
                || keyWordMapper == null) {
            throw new MylException(MylException.Type.NULL_PARAMETER);
        }
        this.imageUrlGenerator = imageUrlGenerator;
        this.collectionMapper = collectionMapper;
        this.rarityMapper     = rarityMapper;
        this.typeMapper       = typeMapper;
        this.raceMapper       = raceMapper;
        this.formatMapper     = formatMapper;
        this.keyWordMapper    = keyWordMapper;
    }

    @NotNull
    public CardDTO toDTO(Card card) {
        CardDTO cardDTO = new CardDTO();
        if (card != null) {
            super.toDTO(card, cardDTO);

            String imageUrl = imageUrlGenerator.generatePresignedUrl(card.getImageUuid());
            cardDTO.setImageUrl(imageUrl);
            cardDTO.setDescription(card.getDescription());
            cardDTO.setCost(card.getCost());
            cardDTO.setDamage(card.getDamage());
            cardDTO.setCollection(collectionMapper.toDTO(card.getCollection()));
            cardDTO.setRarity(rarityMapper.toDTO(card.getRarity()));
            cardDTO.setType(typeMapper.toDTO(card.getType()));
            cardDTO.setRace(raceMapper.toDTO(card.getRace()));
            cardDTO.addFormats(formatMapper.toDTO(card.getFormats()));
            cardDTO.addKeyWords(keyWordMapper.toDTO(card.getKeyWords()));
        }
        return cardDTO;
    }
}
