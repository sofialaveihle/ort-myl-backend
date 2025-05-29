package ar.com.mylback.utils;

import ar.com.mylback.dal.crud.cards.DAOCard;
import ar.com.mylback.utils.entitydtomappers.cards.*;
import com.google.gson.Gson;

import java.util.function.Supplier;

public class InjectorProvider {
    private final Supplier<Gson> gsonSupplier;
    private final Supplier<DAOCard<Integer>> daoCardSupplier;
    private final Supplier<CardMapper> cardMapperSupplier;
    private final Supplier<CollectionMapper> collectionMapperSupplier;
    private final Supplier<RarityMapper> rarityMapperSupplier;
    private final Supplier<FormatMapper> formatMapperSupplier;
    private final Supplier<KeyWordMapper> keyWordMapperSupplier;
    private final Supplier<RaceMapper> raceMapperSupplier;
    private final Supplier<TypeMapper> typeMapperSupplier;

    public InjectorProvider(Supplier<Gson> gsonSupplier,
                            Supplier<DAOCard<Integer>> daoCardSupplier,
                            Supplier<CardMapper> cardMapperSupplier,
                            Supplier<CollectionMapper> collectionMapperSupplier,
                            Supplier<RarityMapper> rarityMapperSupplier,
                            Supplier<FormatMapper> formatMapperSupplier,
                            Supplier<KeyWordMapper> keyWordMapperSupplier,
                            Supplier<RaceMapper> raceMapperSupplier,
                            Supplier<TypeMapper> typeMapperSupplier) throws MylException {

        if (gsonSupplier == null ||
                daoCardSupplier == null ||
                cardMapperSupplier == null ||
                collectionMapperSupplier == null ||
                rarityMapperSupplier == null ||
                formatMapperSupplier == null ||
                keyWordMapperSupplier == null ||
                raceMapperSupplier == null ||
                typeMapperSupplier == null) {
            throw new MylException(MylException.Type.NULL_PARAMETER);
        }

        this.gsonSupplier = gsonSupplier;
        this.daoCardSupplier = daoCardSupplier;
        this.cardMapperSupplier = cardMapperSupplier;
        this.collectionMapperSupplier = collectionMapperSupplier;
        this.rarityMapperSupplier = rarityMapperSupplier;
        this.formatMapperSupplier = formatMapperSupplier;
        this.keyWordMapperSupplier = keyWordMapperSupplier;
        this.raceMapperSupplier = raceMapperSupplier;
        this.typeMapperSupplier = typeMapperSupplier;
    }

    public Gson getGsonSupplier() {
        return gsonSupplier.get();
    }

    public DAOCard<Integer> getDaoCardSupplier() {
        return daoCardSupplier.get();
    }

    public CardMapper getCardMapperSupplier() {
        return cardMapperSupplier.get();
    }

    public CollectionMapper getCollectionMapperSupplier() {
        return collectionMapperSupplier.get();
    }

    public RarityMapper getRarityMapperSupplier() {
        return rarityMapperSupplier.get();
    }

    public FormatMapper getFormatMapperSupplier() {
        return formatMapperSupplier.get();
    }

    public KeyWordMapper getKeyWordMapperSupplier() {
        return keyWordMapperSupplier.get();
    }

    public RaceMapper getRaceMapperSupplier() {
        return raceMapperSupplier.get();
    }

    public TypeMapper getTypeMapperSupplier() {
        return typeMapperSupplier.get();
    }
}
