package ar.com.mylback.utils;

import jakarta.validation.constraints.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QueryString {
    private final Map<String, List<String>> query;

    public QueryString(String query) {
        this.query = parse(query);
    }

    private static String decode(String s) {
        try {
            return java.net.URLDecoder.decode(s, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return s;
        }
    }

    private Map<String, List<String>> parse(String query) {
        Map<String, List<String>> queryParams = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            try {
                for (String pair : query.split("&")) {
                    try {
                        String[] keyValue = pair.split("=", 2);
                        String key = decode(keyValue[0]);
                        String value = keyValue.length > 1 ? decode(keyValue[1]) : "";
                        queryParams.computeIfAbsent(key, v -> new ArrayList<>()).add(value);
                    } catch (Exception e) {
                        System.err.println("Error parsing query key value: " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                System.err.println("Error parsing query string: " + e.getMessage());
            }
        }
        return queryParams;
    }

    @NotNull
    private List<String> getValues(String key) {
        List<String> values = query.get(key);
        if (values == null) {
            values = new ArrayList<>();
        }
        return values;
    }

    @NotNull
    private List<Integer> getIds(String key) {
        return getValues(key).stream()
                .filter(e -> e != null && !e.isEmpty())
                .map(e -> {
                    try {
                        return Integer.parseInt(e);
                    } catch (NumberFormatException ex) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @NotNull
    public List<String> getNames() {
        return getValues(QueryParamKeys.NAME.key());
    }

    public int getPage() {
        List<String> values = getValues(QueryParamKeys.PAGE.key());
        if (values.isEmpty()) {
            return 0;
        }
        return Math.max(Integer.parseInt(values.get(0)), 1);
    }

    public int getPageSize() {
        List<String> values = getValues(QueryParamKeys.PAGE_SIZE.key());
        if (values.isEmpty()) {
            return 0;
        }
        return Math.max(Integer.parseInt(values.get(0)), 1);
    }

    public List<Integer> getCosts() {
        return getIds(QueryParamKeys.COST.key());
    }

    public List<Integer> getDamages() {
        return getIds(QueryParamKeys.DAMAGE.key());
    }

    public List<Integer> getCollectionsIds() {
        return getIds(QueryParamKeys.COLLECTION_ID.key());
    }

    public List<Integer> getRaritiesIds() {
        return getIds(QueryParamKeys.RARITY_ID.key());
    }


    public List<Integer> getTypesIds() {
        return getIds(QueryParamKeys.TYPE_ID.key());
    }

    public List<Integer> getRacesIds() {
        return getIds(QueryParamKeys.RACE_ID.key());
    }

    public List<Integer> getFormatsIds() {
        return getIds(QueryParamKeys.FORMAT_ID.key());
    }

    public List<Integer> getKeyWordsIds() {
        return getIds(QueryParamKeys.KEY_WORD_ID.key());
    }

    private Map<String, List<Integer>> mapOfNonEmptyLists() {
        return Stream.of(
                        new SimpleEntry<>("cost", getCosts()),
                        new SimpleEntry<>("damage", getDamages()),
                        new SimpleEntry<>("collections.collection_id", getCollectionsIds()),
                        new SimpleEntry<>("rarities.rarity_id", getRaritiesIds()),
                        new SimpleEntry<>("types.type_id", getTypesIds()),
                        new SimpleEntry<>("races.race_id", getRacesIds()),
                        new SimpleEntry<>("formats.format_id", getFormatsIds()),
                        new SimpleEntry<>("key_words.key_word_id", getKeyWordsIds()))
                .filter(e -> !e.getValue().isEmpty())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (u, v) -> u            // in case of duplicate keys (shouldn’t happen)
                ));

    }
}
