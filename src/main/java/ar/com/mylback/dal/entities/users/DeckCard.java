package ar.com.mylback.dal.entities.users;

import ar.com.mylback.dal.entities.cards.Card;
import jakarta.persistence.*;

@Entity
@Table(name = "deck_cards")
public class DeckCard {
    @EmbeddedId
    private DeckCardId id;

    @ManyToOne
    @MapsId("deckId")
    @JoinColumn(name = "deck_id")
    private Deck deck;

    @ManyToOne
    @MapsId("cardId")
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(nullable = false)
    private Integer quantity;

    public DeckCardId getId() {
        return id;
    }

    public void setId(DeckCardId id) {
        this.id = id;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
