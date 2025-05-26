package ar.com.mylback.dal.entities.users;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "players")
@AttributeOverrides({
        @AttributeOverride(name = "uuid",
                column = @Column(name = "player_uuid", length = 100)
        )
})
public class Player extends User {
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Deck> decks;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PlayerCard> playerCards = new HashSet<>();
}
