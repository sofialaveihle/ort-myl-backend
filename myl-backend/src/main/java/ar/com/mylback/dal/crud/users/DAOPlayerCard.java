package ar.com.mylback.dal.crud.users;

import ar.com.mylback.dal.crud.DAO;
import ar.com.mylback.dal.crud.HibernateUtil;
import ar.com.mylback.dal.entities.users.PlayerCard;
import ar.com.mylback.dal.entities.users.PlayerCardId;
import ar.com.mylback.utils.MylException;

import java.util.Collection;
import java.util.List;

public class DAOPlayerCard extends DAO<PlayerCard, PlayerCardId> {
    public DAOPlayerCard() {
        super(PlayerCard.class);
    }

    public List<PlayerCard> findAll(String playerUid, int pageNumber, int pageSize) throws MylException {
        return HibernateUtil.withTransaction(session -> {
            List<PlayerCardId> ids = session.createQuery("""
                                SELECT DISTINCT pc.id FROM PlayerCard pc
                                WHERE pc.player.uuid = :uuid
                            """, PlayerCardId.class).setParameter("uuid", playerUid)
                            .setFirstResult((pageNumber - 1) * pageSize)
                            .setMaxResults(pageSize).getResultList();

            if (ids.isEmpty()) return List.of();
            return session.createQuery("""
                    SELECT DISTINCT pc FROM PlayerCard pc
                    LEFT JOIN FETCH pc.card card
                    LEFT JOIN FETCH card.collection
                    LEFT JOIN FETCH card.type
                    LEFT JOIN FETCH card.rarity
                    LEFT JOIN FETCH card.race
                    LEFT JOIN FETCH card.formats
                    LEFT JOIN FETCH card.keyWords
                    WHERE pc.id IN :ids
                    """, PlayerCard.class).setParameter("ids", ids).getResultList();
        });
    }

    @Override
    public void save(Collection<PlayerCard> playerCards) throws MylException {
        HibernateUtil.withTransaction(session -> {
            String sql = "INSERT INTO player_cards (player_uuid, card_id, quantity) VALUES (:playerUuid, :cardId, :quantity)";

            for (PlayerCard playerCard : playerCards) {
                session.createNativeQuery(sql, Void.class)
                        .setParameter("playerUuid", playerCard.getId().getPlayerUuid())
                        .setParameter("cardId", playerCard.getId().getCardId())
                        .setParameter("quantity", playerCard.getQuantity())
                        .executeUpdate();
            }
        });
    }

    @Override
    public void update(PlayerCard playerCard) throws MylException {
        HibernateUtil.withTransaction(session -> {
            return session.createQuery("""
                            UPDATE PlayerCard pc
                            SET pc.quantity = :newQuantity
                            WHERE pc.id = :cardId
                            """)
                    .setParameter("newQuantity", playerCard.getQuantity())
                    .setParameter("cardId", playerCard.getId())
                    .executeUpdate();
        });
    }
}
