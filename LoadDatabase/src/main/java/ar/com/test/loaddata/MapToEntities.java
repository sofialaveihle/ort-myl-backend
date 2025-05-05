package ar.com.test.loaddata;

import ar.com.mylback.MylException;
import ar.com.mylback.dal.crud.DAOCardProperties;
import ar.com.mylback.dal.entities.CardProperties;
import ar.com.test.loaddata.models.*;
import ar.com.test.loaddata.models.Collection;
import ar.com.test.loaddata.models.Properties;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class MapToEntities {

    public static List<ar.com.mylback.dal.entities.Card> mapCardToEntity(Set<Card> cards, CardsData cardsData) throws MylException {
        List<ar.com.mylback.dal.entities.Card> cardsEntities = new ArrayList<>();

        if (cards != null) {
            for (Card card : cards) {
                ar.com.mylback.dal.entities.Card entityCard = new ar.com.mylback.dal.entities.Card();
                entityCard.setName(card.getName());
                entityCard.setDescription(card.getAbility());
                if (card.getCost() != null) {
                    entityCard.setCost(Integer.parseInt(card.getCost()));
                }
                if (card.getDamage() != null) {
                    entityCard.setDamage(Integer.parseInt(card.getDamage()));
                }

                // search and set collection
                entityCard.setCollection(getCollectionFromDB(cardsData.getCollections(), card));

                // search and set rarity
                entityCard.setRarity(getCardPropertiesFromDB(cardsData.getRarities(), card.getRarity(), ar.com.mylback.dal.entities.Rarity.class));

                // search and set type
                entityCard.setType(getCardPropertiesFromDB(cardsData.getTypes(), card.getType(), ar.com.mylback.dal.entities.Type.class));

                // search and set race
                entityCard.setRace(getCardPropertiesFromDB(cardsData.getRaces(), card.getRace(), ar.com.mylback.dal.entities.Race.class));

                // set formats
                entityCard.setFormats(getFormats(card));

                // search and set keyWords
                int containedKeyWords = Integer.parseInt(card.getKeywords() != null ? card.getKeywords() : "0");
                Set<String> keyWordsNames = cardsData.getAllKeyWords().stream()
                        .filter(k -> {
                            int flag = Integer.parseInt(k.getFlag());
                            // keep any keyword whose bit is set in containedKeyWords
                            return (flag & containedKeyWords) != 0;
                        })
                        .map(KeyWord::getTitle)
                        .collect(Collectors.toSet());
                List<ar.com.mylback.dal.entities.KeyWord> keyWords = new DAOCardProperties<>(ar.com.mylback.dal.entities.KeyWord.class).findAllByName(keyWordsNames);
                entityCard.setKeyWords(new HashSet<>(keyWords));

                entityCard.setImageUuid(UUID.randomUUID());


                if (entityCard.getCollection() != null) {
                    cardsEntities.add(entityCard);
                }
            }
        }
        return cardsEntities;
    }

    @NotNull
    private static Set<ar.com.mylback.dal.entities.Format> getFormats(Card card) throws MylException {
        Set<ar.com.mylback.dal.entities.Format> formatEntity = new HashSet<>();
        DAOCardProperties<ar.com.mylback.dal.entities.Format, String> daoCardProperties = new DAOCardProperties<>(ar.com.mylback.dal.entities.Format.class);
        if (card.getFormats() != null) {
            if (card.getFormats().isEmpire()) {
                formatEntity.add(daoCardProperties.findByName("empire"));
            }

            if (card.getFormats().isInfantry()) {
                formatEntity.add(daoCardProperties.findByName("infantry"));
            }

            if (card.getFormats().isVcr()) {
                formatEntity.add(daoCardProperties.findByName("vcr"));
            }

            if (card.getFormats().isFirst_era()) {
                formatEntity.add(daoCardProperties.findByName("first_era"));
            }

            if (card.getFormats().isUnified()) {
                formatEntity.add(daoCardProperties.findByName("unified"));
            }
        }
        return formatEntity;
    }

    private static ar.com.mylback.dal.entities.Collection getCollectionFromDB(Set<Collection> collections, Card card) throws MylException {
        DAOCardProperties<ar.com.mylback.dal.entities.Collection, String> daoCardProperties = new DAOCardProperties<>(ar.com.mylback.dal.entities.Collection.class);
        String name = collections.stream()
                .filter(collection -> collection.getId().equals(card.getEd_edid()))
                .map(Collection::getTitle)
                .findFirst()
                .orElse("");
        if (name.isEmpty()) {
            return null;
        }
        return daoCardProperties.findByName(name);
    }

    /**
     * for rarity, race and types
     */
    private static <T extends CardProperties, Q extends Properties> T getCardPropertiesFromDB(Set<Q> items, String which, Class<T> type) throws MylException {
        DAOCardProperties<T, String> daoCardProperties = new DAOCardProperties<>(type);
        String name = items.stream()
                .filter(itemName -> itemName.getId().equals(which))
                .map(Properties::getName)
                .findFirst()
                .orElse("");
        if (name.isEmpty()) {
            return null;
        }
        return daoCardProperties.findByName(name);
    }

    public static List<ar.com.mylback.dal.entities.Collection> mapCollectionToEntity(CardsData cardsData) {
        List<ar.com.mylback.dal.entities.Collection> collectionsEntity = new ArrayList<>();

        if (cardsData != null) {
            for (Collection collection : cardsData.getCollections()) {
                ar.com.mylback.dal.entities.Collection entityCollection = new ar.com.mylback.dal.entities.Collection();

                entityCollection.setName(collection.getTitle());

                collectionsEntity.add(entityCollection);
            }
        }
        return collectionsEntity;
    }

    public static List<ar.com.mylback.dal.entities.Format> mapFormatToEntity() {
        List<ar.com.mylback.dal.entities.Format> formatEntity = new ArrayList<>();

        ar.com.mylback.dal.entities.Format formatEmpire = new ar.com.mylback.dal.entities.Format();
        formatEmpire.setName("empire");
        formatEntity.add(formatEmpire);

        ar.com.mylback.dal.entities.Format infantry = new ar.com.mylback.dal.entities.Format();
        infantry.setName("infantry");
        formatEntity.add(infantry);


        ar.com.mylback.dal.entities.Format vcr = new ar.com.mylback.dal.entities.Format();
        vcr.setName("vcr");
        formatEntity.add(vcr);

        ar.com.mylback.dal.entities.Format first_era = new ar.com.mylback.dal.entities.Format();
        first_era.setName("first_era");
        formatEntity.add(first_era);

        ar.com.mylback.dal.entities.Format unified = new ar.com.mylback.dal.entities.Format();
        unified.setName("unified");
        formatEntity.add(unified);

        return formatEntity;
    }

    public static List<ar.com.mylback.dal.entities.KeyWord> mapKeyWordsToEntity(CardsData cardsData) {
        List<ar.com.mylback.dal.entities.KeyWord> keyWordsEntities = new ArrayList<>();

        if (cardsData != null) {
            for (KeyWord keyWord : cardsData.getAllKeyWords()) {
                ar.com.mylback.dal.entities.KeyWord entityKeyWord = new ar.com.mylback.dal.entities.KeyWord();

                entityKeyWord.setName(keyWord.getTitle());
                entityKeyWord.setDefinition(keyWord.getDefinition());

                keyWordsEntities.add(entityKeyWord);
            }
        }
        return keyWordsEntities;
    }

    public static List<ar.com.mylback.dal.entities.Race> mapRaceToEntity(CardsData cardsData) {
        List<ar.com.mylback.dal.entities.Race> racesEntity = new ArrayList<>();

        if (cardsData != null) {
            for (Race race : cardsData.getRaces()) {
                ar.com.mylback.dal.entities.Race entityRace = new ar.com.mylback.dal.entities.Race();

                entityRace.setName(race.getName());

                racesEntity.add(entityRace);
            }
        }
        return racesEntity;
    }

    public static List<ar.com.mylback.dal.entities.Rarity> mapRarityToEntity(CardsData cardsData) {
        List<ar.com.mylback.dal.entities.Rarity> raritiesEntity = new ArrayList<>();

        if (cardsData != null) {
            for (Rarity rarity : cardsData.getRarities()) {
                ar.com.mylback.dal.entities.Rarity entityRarity = new ar.com.mylback.dal.entities.Rarity();

                entityRarity.setName(rarity.getName());

                raritiesEntity.add(entityRarity);
            }
        }
        return raritiesEntity;
    }

    public static List<ar.com.mylback.dal.entities.Type> mapTypeToEntity(CardsData cardsData) {
        List<ar.com.mylback.dal.entities.Type> typesEntity = new ArrayList<>();

        if (cardsData != null) {
            for (Type type : cardsData.getTypes()) {
                ar.com.mylback.dal.entities.Type entityType = new ar.com.mylback.dal.entities.Type();

                entityType.setName(type.getName());

                typesEntity.add(entityType);
            }
        }
        return typesEntity;
    }
}
