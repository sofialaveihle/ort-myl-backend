package ar.com.mylback.dal.crud;

import ar.com.mylback.utils.MylException;
import ar.com.mylback.dal.entities.Card;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

public class DAOCard<ID extends Serializable> extends DAO<Card, ID> {

    public DAOCard() {
        super(Card.class);
    }

    public Card findById(ID id) throws MylException {
        return HibernateUtil.withTransaction(session -> {
            Card card = session.get(entityClass, id);
            Hibernate.initialize(card.getFormats());
            Hibernate.initialize(card.getKeyWords());
            return card;
        });
    }

    public List<Card> getAllCardsWithCollections() throws MylException {
        return HibernateUtil.withSession(session -> {
            String hql = "FROM Card c JOIN FETCH c.collection";

            Query<Card> query = session.createQuery(hql, Card.class);
            return query.getResultList();
        });
    }

    public List<Card> findAllPaged(int pageNumber, int pageSize) throws MylException {
        return HibernateUtil.withSession(session -> {
            // Page IDs
            List<Integer> ids = session.createQuery(
                            "SELECT c.id FROM Card c ORDER BY c.id", Integer.class)
                    .setFirstResult((pageNumber - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();

            // Fetch entities + associations
            return session.createQuery("""
                        SELECT DISTINCT c FROM Card c
                        LEFT JOIN FETCH c.collection
                        LEFT JOIN FETCH c.race
                        LEFT JOIN FETCH c.rarity
                        LEFT JOIN FETCH c.type
                        LEFT JOIN FETCH c.formats
                        LEFT JOIN FETCH c.keyWords
                        WHERE c.id IN :ids
                        ORDER BY c.id
                    """, Card.class)
                        .setParameter("ids", ids)
                        .getResultList();
        });
    }
}


