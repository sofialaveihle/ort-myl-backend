package ar.com.mylback.dal.entities.users;

import ar.com.mylback.dal.entities.cards.Card;
import jakarta.persistence.*;

@Entity
@Table(name = "player_cards")
public class PlayerCard {
    @EmbeddedId
    private PlayerCardId id;

    @ManyToOne
    @MapsId("playerUuid")
    @JoinColumn(name = "player_uuid")
    private Player player;

    @ManyToOne
    @MapsId("cardId")
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(nullable = false)
    private Integer quantity;

    public PlayerCardId getId() {
        return id;
    }

    public void setId(PlayerCardId id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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
