package ar.com.mylback.dal.entities.users;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.UUID;

@Embeddable
public class PlayerCardId {
    @Column(name = "player_uuid", columnDefinition = "BINARY(16)")
    private UUID playerUuid;

    @Column(name = "card_id")
    private Integer cardId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerCardId that)) return false;
        return Objects.equals(playerUuid, that.playerUuid) &&
                Objects.equals(cardId, that.cardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerUuid, cardId);
    }
}
