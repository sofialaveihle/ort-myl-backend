package ar.com.mylback.dal.entities.cards;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "cards")
@AttributeOverrides({
        @AttributeOverride(name = "id",
                column = @Column(name = "card_id")
        ),
        @AttributeOverride(name = "name",
                column = @Column(name = "card_name", length = 100)
        )
})
public class Card extends CardProperties {
    @Column(name = "image_uuid", columnDefinition = "BINARY(16)", nullable = false, unique = true)
    private UUID imageUuid;

    @Column(name = "ability", length = 1000)
    private String description;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "damage")
    private Integer damage;

    @ManyToOne
    @JoinColumn(name = "collection_id", nullable = false)
    private Collection collection;

    @ManyToOne
    @JoinColumn(name = "rarity_id")
    private Rarity rarity;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type type;

    @ManyToOne
    @JoinColumn(name = "race_id")
    private Race race;

    @ManyToMany
    @JoinTable(
            name = "cards_formats",
            joinColumns = @JoinColumn(name = "card_id"),
            inverseJoinColumns = @JoinColumn(name = "format_id")
    )
    private Set<Format> formats = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "cards_key_words",
            joinColumns = @JoinColumn(name = "card_id"),
            inverseJoinColumns = @JoinColumn(name = "key_word_id")
    )
    private Set<KeyWord> keyWords = new HashSet<>();

    public Card() {}

    public UUID getImageUuid() {
        return imageUuid;
    }

    public void setImageUuid(UUID imageUuid) {
        this.imageUuid = imageUuid;
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

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Set<Format> getFormats() {
        return formats;
    }

    public void setFormats(Set<Format> formats) {
        this.formats = formats;
    }

    public Set<KeyWord> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(Set<KeyWord> keyWords) {
        this.keyWords = keyWords;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imageUuid=" + imageUuid +
                ", description='" + description + '\'' +
                ", cost=" + cost +
                ", damage=" + damage +
                ", collection=" + collection +
                ", rarity=" + rarity +
                ", type=" + type +
                ", race=" + race +
                ", formats=" + formats +
                ", keyWords=" + keyWords +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}