package ar.com.mylback.dal.crud;

import ar.com.mylback.utils.MylException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class DAO<T, ID extends Serializable> {
    protected final Class<T> entityClass;

    public DAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T findById(ID id) throws MylException {
        return HibernateUtil.withTransaction((Function<Session, T>) session -> session.get(entityClass, id));
    }

    public void save(T entity) throws MylException {
        HibernateUtil.withTransaction((Consumer<Session>) session -> session.persist(entity));
    }

    public void save(Collection<T> entities) throws MylException {
        HibernateUtil.withTransaction(session -> {
            for (T entity : entities) {
                try {
                    session.persist(entity);
                } catch (ConstraintViolationException e) {
                    System.err.println("Error: Duplicate entry found for entity: " + entity + "\n" + e.getMessage());
                }
            }
        });
    }

    public void saveMerge(List<T> entities) throws MylException {
        HibernateUtil.withTransaction(session -> {
            for (T entity : entities) {
                try {
                    session.persist(entity);
                } catch (ConstraintViolationException e) {
                    System.err.println("Error: Duplicate entry found for entity: " + entity + "\n" + e.getMessage());
                }
            }
        });
    }

    public void update(T entity) throws MylException {
        HibernateUtil.withTransaction((Function<Session, T>) session -> session.merge(entity));
    }

    public void delete(T entity) throws MylException {
        HibernateUtil.withTransaction((Consumer<Session>) session -> session.remove(entity));
    }

    public void deleteById(ID id) throws MylException {
        try {
            HibernateUtil.withTransaction(session -> {
                T entity = session.get(entityClass, id);
                if (entity != null) {
                    session.remove(entity);
                } else {
                    throw new RuntimeException("Entidad no encontrada para eliminar ID: " + id);
                }
            });
        } catch (Exception e) {
            throw new MylException(MylException.Type.NOT_FOUND, e.getMessage());
        }
    }
}
