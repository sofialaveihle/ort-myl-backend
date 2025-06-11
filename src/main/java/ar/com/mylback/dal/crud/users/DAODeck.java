package ar.com.mylback.dal.crud.users;

import ar.com.mylback.dal.crud.DAO;
import ar.com.mylback.dal.crud.HibernateUtil;
import ar.com.mylback.dal.entities.users.Deck;
import ar.com.mylback.utils.MylException;
import org.hibernate.query.Query;

import java.util.List;

public class DAODeck extends DAO<Deck, Integer> {

    public DAODeck() {
        super(Deck.class);
    }

    public Deck findById(String playerUid, Integer id) throws MylException {
        return HibernateUtil.withTransaction(session -> {
            return session.createQuery("""
                            SELECT DISTINCT d FROM Deck d
                            LEFT JOIN FETCH d.cards dc
                            LEFT JOIN FETCH dc.card card
                            LEFT JOIN FETCH card.collection
                            LEFT JOIN FETCH card.type
                            LEFT JOIN FETCH card.rarity
                            LEFT JOIN FETCH card.race
                            LEFT JOIN FETCH card.formats
                            LEFT JOIN FETCH card.keyWords
                            WHERE d.id = :id AND d.player.uuid = :uuid
                            """, Deck.class)
                    .setParameter("id", id)
                    .setParameter("uuid", playerUid)
                    .uniqueResult();
        });
    }

    public List<Deck> findAll(String playerUid) throws MylException {
        return HibernateUtil.withTransaction(session -> {
            List<Integer> ids = session.createQuery("""
                        SELECT DISTINCT d.id FROM Deck d
                        WHERE d.player.uuid = :uuid
                    """, Integer.class).setParameter("uuid", playerUid).getResultList();

            if (ids.isEmpty()) return List.of();
            return session.createQuery("""
                    SELECT DISTINCT d FROM Deck d
                    LEFT JOIN FETCH d.cards dc
                    LEFT JOIN FETCH dc.card card
                    LEFT JOIN FETCH card.collection
                    LEFT JOIN FETCH card.type
                    LEFT JOIN FETCH card.rarity
                    LEFT JOIN FETCH card.race
                    LEFT JOIN FETCH card.formats
                    LEFT JOIN FETCH card.keyWords
                    WHERE d.id IN :ids
                    """, Deck.class).setParameter("ids", ids).getResultList();
        });
    }

    @Override
    public void update(Deck deck) throws MylException {
        HibernateUtil.withTransaction(session -> {
            return session.createQuery("""
                                UPDATE Deck d
                                SET d.name = :newName, d.description = :newDescription
                                WHERE d.id = :deckId
                            """)
                    .setParameter("newName", deck.getName())
                    .setParameter("newDescription", deck.getDescription())
                    .setParameter("deckId", deck.getId())
                    .executeUpdate();
        });
    }
}
