package ar.com.mylback.utils;

public enum QueryParamKeys {
    PAGE("page"),
    PAGE_SIZE("pageSize"),
    NAME("name"),
    COST("cost"),
    DAMAGE("damage"),
    COLLECTION_ID("collectionId"),
    RARITY_ID("rarityId"),
    TYPE_ID("typeId"),
    RACE_ID("raceId"),
    FORMAT_ID("formatId"),
    KEY_WORD_ID("keyWordId"),
    VALID("valid");

    private final String key;

    QueryParamKeys(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
