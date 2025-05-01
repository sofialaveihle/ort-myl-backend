package ar.com.mylback.dal.crud;

import ar.com.mylback.MylException;
import ar.com.mylback.dal.entities.CardProperties;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class DAOCardProperties<T extends CardProperties, ID extends Serializable> extends DAO<T, ID> {

    public DAOCardProperties(Class<T> entityClass) {
        super(entityClass);
    }

    public T findByName(String value) throws MylException {
        return HibernateUtil.withSession(session ->
                session.createQuery(
                                "FROM " + getEntityClass().getSimpleName() + " e WHERE e.name = :value",
                                getEntityClass())
                        .setParameter("value", value)
                        .uniqueResult()
        );
    }

    public List<T> findAllByName(Set<String> names) throws MylException {
        return HibernateUtil.withSession(session ->
                session.createQuery(
                                "FROM " + getEntityClass().getSimpleName() + " e WHERE e.name IN :names",      // ① use IN :names
                                getEntityClass())
                        .setParameter("names", names)              // ② bind the List<String>
                        .getResultList()                           // ③ return a List<T>
        );
    }
}
