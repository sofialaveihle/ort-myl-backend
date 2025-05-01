package ar.com.mylback.dal.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "collections")
@AttributeOverrides({
        @AttributeOverride(name = "id",
                column = @Column(name = "collection_id")
        ),
        @AttributeOverride(name = "name",
                column = @Column(name = "collection_name", length = 100)
        )
})
public class Collection extends CardProperties {
    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Card> cards = new HashSet<>();

    public Collection() {
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }
}
