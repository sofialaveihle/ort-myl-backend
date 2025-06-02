package ar.com.mylback.dal.entities.users;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "player_decks")
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deck_id")
    private Integer id;

    @Column(name = "deck_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_uuid", nullable = false)
    private Player player;


    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DeckCard> cards;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Set<DeckCard> getCards() {
        return cards;
    }

    public void setCards(Set<DeckCard> cards) {
        this.cards = cards;
    }
}
