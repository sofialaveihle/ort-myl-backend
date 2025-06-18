package ar.com.mylback.dal.crud.users;

import ar.com.mylback.dal.crud.HibernateUtil;
import ar.com.mylback.dal.entities.users.Player;
import ar.com.mylback.utils.MylException;

public class DAOPlayer extends DAOUser<Player> {

    public DAOPlayer() {
        super(Player.class);
    }

    public Player findByUidWithJoin(String uid) throws MylException {
        return HibernateUtil.withTransaction(session -> {
            return session.createQuery("""
                    SELECT DISTINCT p FROM Player p
                    LEFT JOIN FETCH p.decks d
                    LEFT JOIN FETCH d.cards dc
                    LEFT JOIN FETCH dc.card card
                    LEFT JOIN FETCH card.formats
                    LEFT JOIN FETCH card.keyWords
                    LEFT JOIN FETCH p.playerCards
                    WHERE p.uuid = :uuid
                    """, Player.class).setParameter("uuid", uid).uniqueResult();
        });
    }
}