package ar.com.mylback.dal.crud.users;

import ar.com.mylback.dal.crud.DAO;
import ar.com.mylback.dal.crud.HibernateUtil;
import ar.com.mylback.dal.entities.users.DeckCard;
import ar.com.mylback.dal.entities.users.DeckCardId;
import ar.com.mylback.utils.MylException;

import java.util.Collection;

public class DAODeckCard extends DAO<DeckCard, DeckCardId> {
    public DAODeckCard() {
        super(DeckCard.class);
    }

    @Override
    public void update(DeckCard deckCard) throws MylException {
        HibernateUtil.withTransaction(session -> {
            return session.createQuery("""
                            UPDATE DeckCard dc
                            SET dc.quantity = :newQuantity
                            WHERE dc.id = :deckCardId
                            """)
                    .setParameter("newQuantity", deckCard.getQuantity())
                    .setParameter("deckCardId", deckCard.getId())
                    .executeUpdate();
        });
    }

    @Override
    public void save(Collection<DeckCard> deckCards) throws MylException {
        HibernateUtil.withTransaction(session -> {
            String sql = "INSERT INTO deck_cards (deck_id, card_id, quantity) VALUES (:deckId, :cardId, :quantity)";

            for (DeckCard deckCard : deckCards) {
                session.createNativeQuery(sql, Void.class)
                        .setParameter("deckId", deckCard.getId().getDeckId())
                        .setParameter("cardId", deckCard.getId().getCardId())
                        .setParameter("quantity", deckCard.getQuantity())
                        .executeUpdate();
            }
        });

    }
}
