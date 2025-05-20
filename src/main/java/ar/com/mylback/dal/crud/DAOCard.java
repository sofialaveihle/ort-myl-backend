package ar.com.mylback.dal.crud;

import ar.com.mylback.dal.entities.Card;
import ar.com.mylback.utils.MylException;
import jakarta.persistence.TypedQuery;
import org.hibernate.Hibernate;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

public class DAOCard<ID extends Serializable> extends DAO<Card, ID> {

    public DAOCard() {
        super(Card.class);
    }

    public Card findById(ID id) throws MylException {
        return HibernateUtil.withTransaction(session -> {
            Card card = session.get(entityClass, id);
            Hibernate.initialize(card.getFormats());
            Hibernate.initialize(card.getKeyWords());
            return card;
        });
    }

    public List<Card> getAllCardsWithCollections() throws MylException {
        return HibernateUtil.withSession(session -> {
            String hql = "FROM Card c JOIN FETCH c.collection";

            Query<Card> query = session.createQuery(hql, Card.class);
            return query.getResultList();
        });
    }

    public List<Card> findAllPaged(int pageNumber, int pageSize,
                                   List<Integer> costs, List<Integer> damages,
                                   List<Integer> collectionIds, List<Integer> raritiesIds,
                                   List<Integer> typesIds, List<Integer> racesIds,
                                   List<Integer> formatsIds, List<Integer> keyWordsIds) throws MylException {
        return HibernateUtil.withSession(session -> {
            // Page IDs
            StringBuilder hqlIds = new StringBuilder();
            hqlIds.append("SELECT DISTINCT c.id FROM Card c ");

            List<String> joinClauses = new ArrayList<>();
            List<String> whereClauses = new ArrayList<>();
            Map<String, Object> parameters = new HashMap<>(); // Map to store parameters

            // Add a condition, join (if needed), and parameter
            Consumer<FilterConfig> addFilterCondition = (config) -> {
                if (Objects.nonNull(config.values) && !config.values.isEmpty()) {
                    if (config.requiresJoin) {
                        joinClauses.add(config.joinClause);
                    }
                    whereClauses.add(config.whereClause);
                    parameters.put(config.paramName, config.values);
                }
            };

            // Filter configurations
            addFilterCondition.accept(new FilterConfig("c.cost IN (:costs)", "costs", costs));
            addFilterCondition.accept(new FilterConfig("c.damage IN (:damages)", "damages", damages));
            addFilterCondition.accept(new FilterConfig("c.collection.id IN (:collectionIds)", "collectionIds", collectionIds));
            addFilterCondition.accept(new FilterConfig("c.rarity.id IN (:raritiesIds)", "raritiesIds", raritiesIds));
            addFilterCondition.accept(new FilterConfig("c.type.id IN (:typesIds)", "typesIds", typesIds));
            addFilterCondition.accept(new FilterConfig("c.race.id IN (:racesIds)", "racesIds", racesIds));

            // Filters requiring a JOIN
            addFilterCondition.accept(new FilterConfig("f.id IN (:formatsIds)", "formatsIds", formatsIds, true, "JOIN c.formats f"));
            addFilterCondition.accept(new FilterConfig("kw.id IN (:keyWordsIds)", "keyWordsIds", keyWordsIds, true, "JOIN c.keyWords kw"));


            // Append joins to the HQL string
            for (String join : joinClauses) {
                hqlIds.append(join).append(" ");
            }

            // Append where clauses to the HQL string
            if (!whereClauses.isEmpty()) {
                hqlIds.append("WHERE ");
                hqlIds.append(String.join(" AND ", whereClauses)); // Join conditions with " AND "
            }

            hqlIds.append(" ORDER BY c.id");

            TypedQuery<Integer> idsQuery = session.createQuery(hqlIds.toString(), Integer.class);

            // Set parameters from the map (single loop, no more checks!)
            parameters.forEach(idsQuery::setParameter);

            // Apply pagination
            idsQuery.setFirstResult((pageNumber - 1) * pageSize);
            idsQuery.setMaxResults(pageSize);

            // Execute the first query. Get the paginated filtered list of IDs
            List<Integer> ids = idsQuery.getResultList();

            // Fetch entities + associations
            return session.createQuery("""
                                SELECT DISTINCT c FROM Card c
                                LEFT JOIN FETCH c.collection
                                LEFT JOIN FETCH c.race
                                LEFT JOIN FETCH c.rarity
                                LEFT JOIN FETCH c.type
                                LEFT JOIN FETCH c.formats
                                LEFT JOIN FETCH c.keyWords
                                WHERE c.id IN :ids
                                ORDER BY c.id
                            """, Card.class)
                    .setParameter("ids", ids)
                    .getResultList();
        });
    }

//    private <T> String buildQuery(List<T> values, String dBPropString, String keyParameter, final Map<String, Object> params) throws MylException {
//        if (values != null && !values.isEmpty()
//                && dBPropString != null && !dBPropString.isEmpty()
//                && keyParameter != null && !keyParameter.isEmpty()
//                && params != null && !params.isEmpty()) {
//            params.put(keyParameter, values);
//            return dBPropString + "IN :" + keyParameter;
//        } else {
//            throw new MylException(); // TODO
//        }
//    }
//

    private static class FilterConfig {
        String whereClause;
        String paramName;
        List<?> values;
        boolean requiresJoin;
        String joinClause; // Only relevant if requiresJoin is true

        // Constructor for filters that do NOT require a join
        public FilterConfig(String whereClause, String paramName, List<?> values) {
            this(whereClause, paramName, values, false, null);
        }

        // Constructor for filters that DO require a join
        public FilterConfig(String whereClause, String paramName, List<?> values, boolean requiresJoin, String joinClause) {
            this.whereClause = whereClause;
            this.paramName = paramName;
            this.values = values;
            this.requiresJoin = requiresJoin;
            this.joinClause = joinClause;
        }
    }

}


