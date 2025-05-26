package ar.com.mylback.dal.crud;

import ar.com.mylback.dal.entities.cards.Card;
import ar.com.mylback.utils.MylException;
import jakarta.persistence.TypedQuery;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.*;

public class DAOCard<ID extends Serializable> extends DAO<Card, ID> {

    public DAOCard() {
        super(Card.class);
    }

    public Card findById(ID id) throws MylException {
        return HibernateUtil.withTransaction(session -> {
            return session.createQuery("""
                        SELECT DISTINCT c FROM Card c
                        LEFT JOIN FETCH c.collection
                        LEFT JOIN FETCH c.race
                        LEFT JOIN FETCH c.rarity
                        LEFT JOIN FETCH c.type
                        LEFT JOIN FETCH c.formats
                        LEFT JOIN FETCH c.keyWords
                        WHERE c.id IN :id
                    """, Card.class).setParameter("id", id).uniqueResult();
        });
    }

    public List<Card> getAllCardsWithCollections() throws MylException {
        return HibernateUtil.withSession(session -> {
            String hql = "FROM Card c JOIN FETCH c.collection";

            Query<Card> query = session.createQuery(hql, Card.class);
            return query.getResultList();
        });
    }

    public List<Card> findAllPagedFiltered(int pageNumber, int pageSize,
                                           List<Integer> costs, List<Integer> damages,
                                           List<Integer> collectionIds, List<Integer> raritiesIds,
                                           List<Integer> typesIds, List<Integer> racesIds,
                                           List<Integer> formatsIds, List<Integer> keyWordsIds, List<String> names) throws MylException {
        return HibernateUtil.withSession(session -> {
            HQLFilterBuilder filterBuilder = new HQLFilterBuilder();

            // Filter configurations
            filterBuilder.addFilter(new FilterConfig("c.cost IN (:costs)", "costs", costs));
            filterBuilder.addFilter(new FilterConfig("c.damage IN (:damages)", "damages", damages));
            filterBuilder.addFilter(new FilterConfig("c.collection.id IN (:collectionIds)", "collectionIds", collectionIds));
            filterBuilder.addFilter(new FilterConfig("c.rarity.id IN (:raritiesIds)", "raritiesIds", raritiesIds));
            filterBuilder.addFilter(new FilterConfig("c.type.id IN (:typesIds)", "typesIds", typesIds));
            filterBuilder.addFilter(new FilterConfig("c.race.id IN (:racesIds)", "racesIds", racesIds));

            // Filters requiring a JOIN
            filterBuilder.addFilter(new FilterConfig("f.id IN (:formatsIds)", "formatsIds", formatsIds, true, "JOIN c.formats f"));
            filterBuilder.addFilter(new FilterConfig("kw.id IN (:keyWordsIds)", "keyWordsIds", keyWordsIds, true, "JOIN c.keyWords kw"));

            // Filters with LIKE and OR
            filterBuilder.addLikeConditions("c.name", "name", names);

            String hqlIds = "SELECT DISTINCT c.id FROM Card c "
                    + filterBuilder.getJoins() + " "
                    + filterBuilder.getWhereClause()
                    + " ORDER BY c.id";

            TypedQuery<Integer> idsQuery = session.createQuery(hqlIds, Integer.class);
            filterBuilder.getParameters().forEach(idsQuery::setParameter);
            idsQuery.setFirstResult((pageNumber - 1) * pageSize);
            idsQuery.setMaxResults(pageSize);

            // Get the paginated filtered list of IDs
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

    private static class HQLFilterBuilder {

        private final List<String> joinClauses = new ArrayList<>();
        private final List<String> whereClauses = new ArrayList<>();
        private final Map<String, Object> parameters = new HashMap<>();

        public void addFilter(FilterConfig config) {
            if (config.values != null && !config.values.isEmpty()) {
                if (config.requiresJoin) {
                    joinClauses.add(config.joinClause);
                }
                whereClauses.add(config.whereClause);
                parameters.put(config.paramName, config.values);
            }
        }

        public void addLikeConditions(String field, String paramPrefix, List<String> values) {
            if (values != null && !values.isEmpty()) {
                List<String> conditions = new ArrayList<>();
                for (int i = 0; i < values.size(); i++) {
                    String paramName = paramPrefix + i;
                    conditions.add("LOWER(" + field + ") LIKE :" + paramName);
                    parameters.put(paramName, "%" + values.get(i).toLowerCase() + "%");
                }
                whereClauses.add("(" + String.join(" OR ", conditions) + ")");
            }
        }

        public String getJoins() {
            return String.join(" ", joinClauses);
        }

        public String getWhereClause() {
            return whereClauses.isEmpty() ? "" : "WHERE " + String.join(" AND ", whereClauses);
        }

        public Map<String, Object> getParameters() {
            return parameters;
        }
    }
}


