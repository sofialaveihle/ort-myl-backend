package ar.com.mylback.dal.crud;

import ar.com.mylback.MylException;
import ar.com.mylback.dal.entities.Card;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.List;

public class DAOCard<T extends Card, ID extends Serializable> extends DAO<T, ID> {

    public DAOCard(Class<T> entityClass) {
        super(entityClass);
    }

    public List<Card> getAllCardsWithCollections() throws MylException {
        return HibernateUtil.withSession(session -> {
            String hql = "FROM Card c JOIN FETCH c.collection";

            Query<Card> query = session.createQuery(hql, Card.class);
            return query.getResultList();
        });
    }
}


