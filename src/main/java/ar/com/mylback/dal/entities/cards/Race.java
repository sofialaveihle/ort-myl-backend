package ar.com.mylback.dal.entities.cards;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "races")
@AttributeOverrides({
        @AttributeOverride(name = "id",
                column = @Column(name = "race_id")
        ),
        @AttributeOverride(name = "name",
                column = @Column(name = "race_name", length = 50)
        )
})
public class Race extends CardProperties {
    @OneToMany(mappedBy = "race", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Card> cards = new HashSet<>();

    public Race() {
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }
}
