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
            Query<Card> query = session.createQuery("FROM Card c", Card.class);


            query.setFirstResult((pageNumber - 1) * pageSize);
            query.setMaxResults(pageSize);
            List<Card> cards = query.getResultList();

            for (Card card : cards) {
                Hibernate.initialize(card.getFormats());
                Hibernate.initialize(card.getKeyWords());
            }

            return query.getResultList();
        });
    }
}


