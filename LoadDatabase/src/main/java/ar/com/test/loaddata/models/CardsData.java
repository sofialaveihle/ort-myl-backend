package ar.com.test.loaddata.models;

import java.util.Set;

public class CardsData {
    private Set<Card> cards;
    private Set<KeyWord> allKeyWords;
    private Set<Type> types;
    private Set<Race> races;
    private Set<Rarity> rarities;
    private Set<Collection> collections;

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public Set<KeyWord> getAllKeyWords() {
        return allKeyWords;
    }

    public void setAllKeyWords(Set<KeyWord> allKeyWords) {
        this.allKeyWords = allKeyWords;
    }

    public Set<Type> getTypes() {
        return types;
    }

    public void setTypes(Set<Type> types) {
        this.types = types;
    }

    public Set<Race> getRaces() {
        return races;
    }

    public void setRaces(Set<Race> races) {
        this.races = races;
    }

    public Set<Rarity> getRarities() {
        return rarities;
    }

    public void setRarities(Set<Rarity> rarities) {
        this.rarities = rarities;
    }

    public Set<Collection> getCollections() {
        return collections;
    }

    public void setCollections(Set<Collection> collections) {
        this.collections = collections;
    }
}
