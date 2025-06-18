package ar.com.mylback.dal.entities.cards;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rarities")
@AttributeOverrides({
        @AttributeOverride(name = "id",
                column = @Column(name = "rarity_id")
        ),
        @AttributeOverride(name = "name",
                column = @Column(name = "rarity_name", length = 50)
        )
})
public class Rarity extends CardProperties {
    @OneToMany(mappedBy = "rarity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Card> cards = new HashSet<>();

    public Rarity() {
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }
}
