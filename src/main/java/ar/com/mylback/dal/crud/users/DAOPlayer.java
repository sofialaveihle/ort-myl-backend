package ar.com.mylback.dal.crud.users;

import ar.com.mylback.dal.crud.HibernateUtil;
import ar.com.mylback.dal.entities.users.Player;
import ar.com.mylback.utils.MylException;
import org.hibernate.Session;

import java.util.function.Function;

public class DAOPlayer extends DAOUser<Player> {

    public DAOPlayer() {
        super(Player.class);
    }

    public Player findByUuidWithAssociations(String uuid) throws MylException {
        return HibernateUtil.withTransaction((Function<Session, Player>) session ->
                session.createQuery("""
        SELECT DISTINCT p FROM Player p
        LEFT JOIN FETCH p.decks
        LEFT JOIN FETCH p.playerCards
        WHERE p.uuid = :uuid
    """, Player.class)
                        .setParameter("uuid", uuid)
                        .uniqueResult()
        );

    }
}