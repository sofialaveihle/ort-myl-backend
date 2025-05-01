package ar.com.mylback.dal.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "formats")
@AttributeOverrides({
        @AttributeOverride(name = "id",
                column = @Column(name = "format_id")
        ),
        @AttributeOverride(name = "name",
                column = @Column(name = "format_name", length = 100)
        )
})
public class Format extends CardProperties {
    @ManyToMany(mappedBy = "formats")
    private Set<Card> cards = new HashSet<>();

    public Format() {}

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }
}
