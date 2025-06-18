package ar.com.mylback.dal.entities.users;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "players")
@AttributeOverrides({
        @AttributeOverride(name = "uuid",
                column = @Column(name = "player_uuid", length = 128)
        )
})
public class Player extends User {
    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin = false;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Deck> decks;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PlayerCard> playerCards = new HashSet<>();

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Set<Deck> getDecks() {
        return decks;
    }

    public void setDecks(Set<Deck> decks) {
        this.decks = decks;
    }

    public Set<PlayerCard> getPlayerCards() {
        return playerCards;
    }

    public void setPlayerCards(Set<PlayerCard> playerCards) {
        this.playerCards = playerCards;
    }
}
