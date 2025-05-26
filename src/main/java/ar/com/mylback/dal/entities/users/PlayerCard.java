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
    private int quantity;
}
