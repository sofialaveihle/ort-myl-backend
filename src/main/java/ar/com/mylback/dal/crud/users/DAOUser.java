package ar.com.mylback.dal.crud.users;

import ar.com.mylback.dal.crud.cards.DAO;
import ar.com.mylback.dal.crud.HibernateUtil;
import ar.com.mylback.dal.entities.users.User;
import ar.com.mylback.utils.MylException;

public class DAOUser<T extends User> extends DAO<T, String> {

    public DAOUser(Class<T> clazz) {super(clazz);
    }

    public T findByEmail(String email) throws MylException {
        return HibernateUtil.withSession(session ->
                session.createQuery("FROM " + entityClass.getSimpleName() + " u WHERE u.email = :email", entityClass)
                        .setParameter("email", email)
                        .uniqueResult()
        );
    }

    public boolean existsByEmail(String email) throws MylException {
        return HibernateUtil.withSession(session ->
                session.createQuery("SELECT count(u) FROM " + entityClass.getSimpleName() + " u WHERE u.email = :email", Long.class)
                        .setParameter("email", email)
                        .uniqueResult() > 0
        );
    }

    public T findByUuid(String uuid) throws MylException {
        return HibernateUtil.withSession(session ->
                session.createQuery("FROM " + entityClass.getSimpleName() + " u WHERE u.uuid = :uuid", entityClass)
                        .setParameter("uuid", uuid)
                        .uniqueResult()
        );
    }

}