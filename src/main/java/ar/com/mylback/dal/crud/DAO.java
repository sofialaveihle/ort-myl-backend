package ar.com.mylback.dal.crud;

import ar.com.mylback.MylException;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class DAO<T, ID extends Serializable> {
    private final Class<T> entityClass;

    public DAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T findById(ID id) throws MylException {
        return HibernateUtil.withSession(session -> session.get(entityClass, id));
    }

    public void save(T entity) throws MylException {
        HibernateUtil.withTransaction((Consumer<Session>) session -> session.persist(entity));
    }

    public void save(List<T> entities) throws MylException {
        HibernateUtil.withTransaction(session -> {
            entities.forEach(session::persist);
        });
    }

    public void saveMerge(List<T> entities) throws MylException {
        HibernateUtil.withTransaction(session -> {
            for (T entity : entities) {
                session.merge(entity);
            }
        });
    }

    public void update(T entity) throws MylException {
        HibernateUtil.withTransaction((Function<Session, T>) session -> session.merge(entity));
    }

    public void delete(T entity) throws MylException {
        HibernateUtil.withTransaction((Consumer<Session>) session -> session.remove(entity));
    }

    protected Class<T> getEntityClass() {
        return entityClass;
    }
}
