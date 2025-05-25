package ar.com.mylback;

import ar.com.mylback.dal.entities.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.UUID;

public class Test {
    /*public static void main(String[] args) {
        System.out.println(Runtime.version());
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Card.class)
                .addAnnotatedClass(Collection.class)
                .addAnnotatedClass(Format.class)
                .addAnnotatedClass(KeyWord.class)
                .addAnnotatedClass(Race.class)
                .addAnnotatedClass(Rarity.class)
                .addAnnotatedClass(Type.class)
                .buildSessionFactory();

        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();

            // Create related entities
            Collection collection = new Collection();
            collection.setName("Primer Bloque");
            session.persist(collection);

            Rarity rarity = new Rarity();
            rarity.setName("Ultra Real");
            session.persist(rarity);

            Race race = new Race();
            race.setName("Bestia");
            session.persist(race);

            Type type = new Type();
            type.setName("Aliado");
            session.persist(type);

            Format format = new Format();
            format.setName("Imperio");
            session.persist(format);

            KeyWord keyWord = new KeyWord();
            keyWord.setName("Furia");
            keyWord.setDefinition("descripcion de furia");
            session.persist(keyWord);

            // Create the card
            Card card = new Card();
            card.setName("Bestia de Furia");
            card.setImageUuid(UUID.randomUUID());
            card.setDescription("Inflige doble daño si tiene Furia.");
            card.setCost(4);
            card.setDamage(8);
            card.setCollection(collection);
            card.setRarity(rarity);
            card.setRace(race);
            card.setType(type);
            card.getFormats().add(format);
            card.getKeyWords().add(keyWord);

            // Bidirectional (optional, good practice)
            collection.getCards().add(card);
            rarity.getCards().add(card);
            race.getCards().add(card);
            type.getCards().add(card);
            format.getCards().add(card);
            keyWord.getCards().add(card);

            // Persist (Card cascades will persist related entities)
            session.persist(card);

            session.getTransaction().commit();
            System.out.println("Carta guardada correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
            sessionFactory.close();
        }
    }*/
}
