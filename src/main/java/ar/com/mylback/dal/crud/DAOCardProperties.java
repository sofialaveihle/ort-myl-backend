package ar.com.mylback.dal.crud;

import ar.com.mylback.utils.MylException;
import ar.com.mylback.dal.entities.cards.CardProperties;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class DAOCardProperties<T extends CardProperties, ID extends Serializable> extends DAO<T, ID> {

    public DAOCardProperties(Class<T> entityClass) {
        super(entityClass);
    }

    public T findByName(String value) throws MylException {
        return HibernateUtil.withTransaction((Function<Session, T>) session ->
                session.createQuery(
                                "FROM " + entityClass.getSimpleName() + " e WHERE e.name = :value", entityClass)
                        .setParameter("value", value)
                        .uniqueResult()
        );
    }

    public List<T> findAllByName(Set<String> names) throws MylException {
        return HibernateUtil.withTransaction((Function<Session, List<T>>) session ->
                session.createQuery(
                                "FROM " + entityClass.getSimpleName() + " e WHERE e.name IN :names", entityClass)
                        .setParameter("names", names)
                        .getResultList()                                    // return a List<T>
        );
    }

    public List<T> findAll() throws MylException {
        return HibernateUtil.withTransaction((Function<Session, List<T>>) session ->
                session.createQuery("FROM " + entityClass.getSimpleName() + " e ORDER BY e.id", entityClass).getResultList()
        );
    }
}
