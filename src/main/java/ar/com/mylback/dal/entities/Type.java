package ar.com.mylback.dal.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "types")
@AttributeOverrides({
        @AttributeOverride(name = "id",
                column = @Column(name = "type_id")
        ),
        @AttributeOverride(name = "name",
                column = @Column(name = "type_name", length = 50)
        )
})
public class Type extends CardProperties {
    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Card> cards = new HashSet<>();

    public Type() {}

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }
}