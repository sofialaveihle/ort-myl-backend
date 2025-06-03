package ar.com.mylback.dal.crud.users;

import ar.com.mylback.dal.crud.HibernateUtil;
import ar.com.mylback.dal.entities.users.Store;
import ar.com.mylback.utils.MylException;


import java.util.List;

public class DAOStore extends DAOUser<Store> {
    public DAOStore() {
        super(Store.class);
    }

    public List<Store> findAllByValidationStatus(boolean isValid) throws MylException {
        return HibernateUtil.withSession(session ->
                session.createQuery("FROM Store s WHERE s.isValid = :isValid", Store.class)
                        .setParameter("isValid", isValid)
                        .getResultList()
        );
    }

}