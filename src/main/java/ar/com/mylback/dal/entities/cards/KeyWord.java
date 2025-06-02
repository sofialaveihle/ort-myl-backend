package ar.com.mylback.dal.entities.cards;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "key_words")
@AttributeOverrides({
        @AttributeOverride(name = "id",
                column = @Column(name = "key_word_id")
        ),
        @AttributeOverride(name = "name",
                column = @Column(name = "key_word_name", length = 50)
        )
})
public class KeyWord extends CardProperties {
    @Column(name = "definition", length = 500, nullable = false, unique = true)
    private String definition;

    @ManyToMany(mappedBy = "keyWords")
    private Set<Card> cards = new HashSet<>();

    public KeyWord() {
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }
}
