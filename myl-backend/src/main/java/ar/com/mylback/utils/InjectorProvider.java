package ar.com.mylback.utils;

import ar.com.mylback.auth.FirebaseAuthValidator;
import ar.com.mylback.dal.crud.cards.DAOCard;
import ar.com.mylback.dal.crud.cards.DAOCardProperties;
import ar.com.mylback.dal.crud.users.*;
import ar.com.mylback.dal.entities.cards.*;
import ar.com.mylback.utils.entitydtomappers.cards.*;
import ar.com.mylback.utils.entitydtomappers.users.*;
import com.google.gson.Gson;

import java.util.function.Supplier;

public class InjectorProvider {
    private final Supplier<Gson> gsonSupplier;
    private final Supplier<FirebaseAuthValidator> firebaseAuthValidatorSupplier;
    private final Supplier<DAOCard<Integer>> daoCardSupplier;
    private final Supplier<DAOCardProperties<Collection, Integer>> daoCollectionSupplier;
    private final Supplier<DAOCardProperties<Format, Integer>> daoFormatSupplier;
    private final Supplier<DAOCardProperties<KeyWord, Integer>> daoKeyWordSupplier;
    private final Supplier<DAOCardProperties<Race, Integer>> daoRaceSupplier;
    private final Supplier<DAOCardProperties<Rarity, Integer>> daoRaritySupplier;
    private final Supplier<DAOCardProperties<Type, Integer>> daoTypeSupplier;
    private final Supplier<DAOPlayer> daoPlayerSupplier;
    private final Supplier<DAOStore> daoStoreSupplier;
    private final Supplier<DAODeck> daoDeckSupplier;
    private final Supplier<DAODeckCard> daoDeckCardSupplier;
    private final Supplier<DAOPlayerCard> daoPlayerCardSupplier;
    private final Supplier<CardMapper> cardMapperSupplier;
    private final Supplier<UserMapper> userMapperSupplier;
    private final Supplier<PlayerMapper> playerMapperSupplier;
    private final Supplier<StoreMapper> storeMapperSupplier;
    private final Supplier<CollectionMapper> collectionMapperSupplier;
    private final Supplier<RarityMapper> rarityMapperSupplier;
    private final Supplier<FormatMapper> formatMapperSupplier;
    private final Supplier<KeyWordMapper> keyWordMapperSupplier;
    private final Supplier<RaceMapper> raceMapperSupplier;
    private final Supplier<TypeMapper> typeMapperSupplier;
    private final Supplier<DeckMapper> deckMapperSupplier;
    private final Supplier<PlayerCardMapper> playerCardMapperSupplier;

    public InjectorProvider(Supplier<Gson> gsonSupplier,
                            Supplier<FirebaseAuthValidator> firebaseAuthValidatorSupplier,
                            Supplier<DAOCard<Integer>> daoCardSupplier,
                            Supplier<DAOCardProperties<Collection, Integer>> daoCollectionSupplier,
                            Supplier<DAOCardProperties<Format, Integer>> daoFormatSupplier,
                            Supplier<DAOCardProperties<KeyWord, Integer>> daoKeyWordSupplier,
                            Supplier<DAOCardProperties<Race, Integer>> daoRaceSupplier,
                            Supplier<DAOCardProperties<Rarity, Integer>> daoRaritySupplier,
                            Supplier<DAOCardProperties<Type, Integer>> daoTypeSupplier,
                            Supplier<DAOPlayer> daoPlayerSupplier,
                            Supplier<DAOStore> daoStoreSupplier,
                            Supplier<DAODeck> daoDeckSupplier,
                            Supplier<DAODeckCard> daoDeckCardSupplier,
                            Supplier<DAOPlayerCard> daoPlayerCardSupplier,
                            Supplier<CardMapper> cardMapperSupplier,
                            Supplier<UserMapper> userMapperSupplier,
                            Supplier<PlayerMapper> playerMapperSupplier,
                            Supplier<StoreMapper> storeMapperSupplier,
                            Supplier<CollectionMapper> collectionMapperSupplier,
                            Supplier<RarityMapper> rarityMapperSupplier,
                            Supplier<FormatMapper> formatMapperSupplier,
                            Supplier<KeyWordMapper> keyWordMapperSupplier,
                            Supplier<RaceMapper> raceMapperSupplier,
                            Supplier<TypeMapper> typeMapperSupplier,
                            Supplier<DeckMapper> deckMapperSupplier,
                            Supplier<PlayerCardMapper> playerCardMapperSupplier) throws MylException {

        if (gsonSupplier == null ||
                firebaseAuthValidatorSupplier == null ||
                daoCardSupplier == null ||
                daoCollectionSupplier == null ||
                daoFormatSupplier == null ||
                daoKeyWordSupplier == null ||
                daoRaceSupplier == null ||
                daoRaritySupplier == null ||
                daoTypeSupplier == null ||
                daoPlayerSupplier == null ||
                daoStoreSupplier == null ||
                daoDeckSupplier == null ||
                daoDeckCardSupplier == null ||
                daoPlayerCardSupplier == null ||
                cardMapperSupplier == null ||
                userMapperSupplier == null ||
                playerMapperSupplier == null ||
                storeMapperSupplier == null ||
                collectionMapperSupplier == null ||
                rarityMapperSupplier == null ||
                formatMapperSupplier == null ||
                keyWordMapperSupplier == null ||
                raceMapperSupplier == null ||
                typeMapperSupplier == null ||
                deckMapperSupplier == null ||
                playerCardMapperSupplier == null) {
            throw new MylException(MylException.Type.NULL_PARAMETER);
        }

        this.gsonSupplier = gsonSupplier;
        this.firebaseAuthValidatorSupplier = firebaseAuthValidatorSupplier;
        this.daoCardSupplier = daoCardSupplier;
        this.daoCollectionSupplier = daoCollectionSupplier;
        this.daoFormatSupplier = daoFormatSupplier;
        this.daoKeyWordSupplier = daoKeyWordSupplier;
        this.daoRaceSupplier = daoRaceSupplier;
        this.daoRaritySupplier = daoRaritySupplier;
        this.daoTypeSupplier = daoTypeSupplier;
        this.daoPlayerSupplier = daoPlayerSupplier;
        this.daoStoreSupplier = daoStoreSupplier;
        this.daoDeckSupplier = daoDeckSupplier;
        this.daoDeckCardSupplier = daoDeckCardSupplier;
        this.daoPlayerCardSupplier = daoPlayerCardSupplier;
        this.cardMapperSupplier = cardMapperSupplier;
        this.userMapperSupplier = userMapperSupplier;
        this.playerMapperSupplier = playerMapperSupplier;
        this.storeMapperSupplier = storeMapperSupplier;
        this.collectionMapperSupplier = collectionMapperSupplier;
        this.rarityMapperSupplier = rarityMapperSupplier;
        this.formatMapperSupplier = formatMapperSupplier;
        this.keyWordMapperSupplier = keyWordMapperSupplier;
        this.raceMapperSupplier = raceMapperSupplier;
        this.typeMapperSupplier = typeMapperSupplier;
        this.deckMapperSupplier = deckMapperSupplier;
        this.playerCardMapperSupplier = playerCardMapperSupplier;
    }

    public Gson getGson() {
        return gsonSupplier.get();
    }

    public FirebaseAuthValidator getFirebaseAuthValidator() {
        return firebaseAuthValidatorSupplier.get();
    }

    public DAOCard<Integer> getDaoCard() {
        return daoCardSupplier.get();
    }

    public Supplier<DAOCard<Integer>> getDaoCardSupplier() {
        return daoCardSupplier;
    }

    public DAOCardProperties<Collection, Integer> getDaoCollection() {
        return daoCollectionSupplier.get();
    }

    public DAOCardProperties<Format, Integer> getDaoFormat() {
        return daoFormatSupplier.get();
    }

    public DAOCardProperties<KeyWord, Integer> getDaoKeyWord() {
        return daoKeyWordSupplier.get();
    }

    public DAOCardProperties<Race, Integer> getDaoRace() {
        return daoRaceSupplier.get();
    }

    public DAOCardProperties<Rarity, Integer> getDaoRarity() {
        return daoRaritySupplier.get();
    }

    public DAOCardProperties<Type, Integer> getDaoType() {
        return daoTypeSupplier.get();
    }

    public DAOPlayer getDaoPlayer() {
        return daoPlayerSupplier.get();
    }

    public DAOStore getDaoStore() {
        return daoStoreSupplier.get();
    }

    public DAODeck getDaoDeck() {
        return daoDeckSupplier.get();
    }

    public DAODeckCard getDaoDeckCard() {
        return daoDeckCardSupplier.get();
    }

    public CardMapper getCardMapper() {
        return cardMapperSupplier.get();
    }

    public DAOPlayerCard getDaoPlayerCard() {
        return daoPlayerCardSupplier.get();
    }

    public CollectionMapper getCollectionMapper() {
        return collectionMapperSupplier.get();
    }

    public RarityMapper getRarityMapper() {
        return rarityMapperSupplier.get();
    }

    public UserMapper getUserMapper() {
        return userMapperSupplier.get();
    }

    public PlayerMapper getPlayerMapper() {
        return playerMapperSupplier.get();
    }

    public StoreMapper getStoreMapper() {
        return storeMapperSupplier.get();
    }

    public FormatMapper getFormatMapper() {
        return formatMapperSupplier.get();
    }

    public KeyWordMapper getKeyWordMapper() {
        return keyWordMapperSupplier.get();
    }

    public RaceMapper getRaceMapper() {
        return raceMapperSupplier.get();
    }

    public TypeMapper getTypeMapper() {
        return typeMapperSupplier.get();
    }

    public DeckMapper getDeckMapper() {
        return deckMapperSupplier.get();
    }

    public PlayerCardMapper getPlayerCardMapper() {
        return playerCardMapperSupplier.get();
    }
}
