package ar.com.mylback.dal.entities.users;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "player_decks")
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deck_id")
    private int id;

    @Column(name = "deck_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_uuid", nullable = false)
    private Player player;


    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DeckCard> cards;
}
