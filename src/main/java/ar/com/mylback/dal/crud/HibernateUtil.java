package ar.com.mylback.dal.crud;

import ar.com.mylback.dal.entities.cards.*;
import ar.com.mylback.dal.entities.users.*;
import ar.com.mylback.utils.MylException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.function.Consumer;
import java.util.function.Function;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    private HibernateUtil() { }

    public static synchronized SessionFactory getSessionFactory() throws MylException {
        if (sessionFactory == null) {
            try {
                sessionFactory = new Configuration()
                        .configure("hibernate.cfg.xml")
                        .addAnnotatedClass(Card.class)
                        .addAnnotatedClass(Collection.class)
                        .addAnnotatedClass(Format.class)
                        .addAnnotatedClass(KeyWord.class)
                        .addAnnotatedClass(Race.class)
                        .addAnnotatedClass(Rarity.class)
                        .addAnnotatedClass(Type.class)
                        .addAnnotatedClass(User.class)
                        .addAnnotatedClass(Player.class)
                        .addAnnotatedClass(Store.class)
                        .buildSessionFactory();
            } catch (HibernateException ex) {
                throw new MylException();
            }
        }
        return sessionFactory;
    }

    public static Session openSession() throws MylException {
        return getSessionFactory().openSession();
    }

    public static synchronized void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }

    public static <R> R withTransaction(Function<Session, R> work) throws MylException {
        Transaction tx = null;
        try (Session session = openSession()) {
            tx = session.beginTransaction();
            R result = work.apply(session);
            tx.commit();
            return result;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            System.err.println(e.getMessage());
            throw new MylException(MylException.Type.ERROR_DB_TRANSACTION, e.getMessage());
        }
    }

    public static <R> R withSession(Function<Session, R> work) throws MylException {
        try (Session session = openSession()) {
            return work.apply(session);
        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            throw new MylException(MylException.Type.ERROR_DB_TRANSACTION, e.getMessage());
        }
    }

    public static void withTransaction(Consumer<Session> work) throws MylException {
        withTransaction(s -> {
            work.accept(s);
            return null;
        });
    }
}
