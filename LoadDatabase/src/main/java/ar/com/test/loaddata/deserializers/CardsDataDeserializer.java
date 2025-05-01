package ar.com.test.loaddata.deserializers;

import ar.com.test.loaddata.models.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class CardsDataDeserializer implements JsonDeserializer<CardsData> {

    @Override
    public CardsData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        CardsData cardsData = new CardsData();

        loadCardData(cardsData::setAllKeyWords, jsonObject, "keywords", KeyWord.class, context);
        loadCardData(cardsData::setRaces, jsonObject, "races", Race.class, context);
        loadCardData(cardsData::setRarities, jsonObject, "rarities", Rarity.class, context);
        loadCardData(cardsData::setTypes, jsonObject, "types", ar.com.test.loaddata.models.Type.class, context);
        loadCardData(cardsData::setCards, jsonObject, "cards", Card.class, context);


        return cardsData;
    }

    private <T extends Serializable> void loadCardData(Consumer<Set<T>> consumer, JsonObject jsonObject, String jsonKey, Class<T> elementType, JsonDeserializationContext context) throws JsonParseException {
        JsonElement jsonElement = jsonObject.get(jsonKey); // Renamed from keyWordsElement for clarity
        if (jsonElement != null && jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();

            // Use TypeToken to get the Type for List<T>
            Type listType = TypeToken.getParameterized(List.class, elementType).getType();
            Set<T> elements = new HashSet<>(context.deserialize(jsonArray, listType));
            consumer.accept(elements);
        } else {
            consumer.accept(new HashSet<>());
        }
    }
}
