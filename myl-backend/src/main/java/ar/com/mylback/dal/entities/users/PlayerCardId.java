package ar.com.mylback.dal.entities.users;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class PlayerCardId implements Serializable {
    @Column(name = "player_uuid", length = 128)
    private String playerUuid;

    @Column(name = "card_id")
    private Integer cardId;

    public String getPlayerUuid() {
        return playerUuid;
    }

    public void setPlayerUuid(String playerUuid) {
        this.playerUuid = playerUuid;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

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
