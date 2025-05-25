package myldtos.cards;


import java.io.Serializable;
import java.util.*;

public class CardDTO extends CardPropertiesDTO implements Serializable {
    private String imageUrl;
    private String description;
    private Integer cost;
    private Integer damage;
    private CollectionDTO collection;
    private RarityDTO rarity;
    private TypeDTO type;
    private RaceDTO race;
    private final List<FormatDTO> formats = new ArrayList<>();
    private final List<KeyWordDTO> keyWords = new ArrayList<>();

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }

    public CollectionDTO getCollection() {
        return collection;
    }

    public void setCollection(CollectionDTO collection) {
        this.collection = collection;
    }

    public RarityDTO getRarity() {
        return rarity;
    }

    public void setRarity(RarityDTO rarity) {
        this.rarity = rarity;
    }

    public TypeDTO getType() {
        return type;
    }

    public void setType(TypeDTO type) {
        this.type = type;
    }

    public RaceDTO getRace() {
        return race;
    }

    public void setRace(RaceDTO race) {
        this.race = race;
    }

    public FormatDTO getFormatByIndex(int index) {
        return formats.get(index);
    }

//    public FormatDTO getFormatById(String id) {
//        return formats;
//    }
//
//    @NotNull
//    public FormatDTO getFormatByName(String name) {
//        return formats;
//    }

    public void addFormats(Collection<FormatDTO> formats) {
        this.formats.addAll(formats);
    }

    public void addFormat(FormatDTO format) {
        this.formats.add(format);
    }

    public KeyWordDTO getKeyWordByIndex(int index) {
        return keyWords.get(index);
    }

//    public KeyWordDTO getKeyWordById(Integer id) {
//        return keyWords.get(index);
//    }
//
//    public KeyWordDTO getKeyWordByName(String name) {
//        return keyWords.get(index);
//    }

    public void addKeyWords(Collection<KeyWordDTO> keyWords) {
        this.keyWords.addAll(keyWords);
    }

    public void addKeyWord(KeyWordDTO keyWord) {
        this.keyWords.add(keyWord);
    }
}
