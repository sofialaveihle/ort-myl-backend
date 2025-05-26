package ar.com.mylback.dal.entities.users;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class DeckCardId {
    @Column(name = "deck_id")
    private Integer deckId;

    @Column(name = "card_id")
    private Integer cardId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeckCardId that)) return false;
        return Objects.equals(deckId, that.deckId) &&
                Objects.equals(cardId, that.cardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deckId, cardId);
    }
}
