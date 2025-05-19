package ar.com.mylback.utils;

import jakarta.validation.constraints.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.*;

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
            for (String pair : query.split("&")) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    String key = decode(keyValue[0]);
                    String value = decode(keyValue[1]);
                    queryParams.computeIfAbsent(key, v -> new ArrayList<>()).add(value);
                }
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

    public int getPage() {
        List<String> values = getValues("page");
        if (values.isEmpty()) {
            return 0;
        }
        return Math.max(Integer.parseInt(values.get(0)), 1);
    }

    public int getPageSize() {
        List<String> values = getValues("pageSize");
        if (values.isEmpty()) {
            return 0;
        }
        return Math.max(Integer.parseInt(values.get(0)), 1);
    }

    public List<Integer> getCosts() {
        return getValues("cost").stream()
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

    public List<Integer> getDamages() {
        return getValues("damage").stream()
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

    public List<String> getCollections() {
        return getValues("collection").stream()
                .filter(e -> e != null && !e.isEmpty()).toList();
    }

    public List<String> getRarities() {
        return getValues("rarity").stream()
                .filter(e -> e != null && !e.isEmpty()).toList();
    }


    public List<String> getTypes() {
        return getValues("type").stream()
                .filter(e -> e != null && !e.isEmpty()).toList();
    }

    public List<String> getDescriptions() {
        return getValues("race").stream()
                .filter(e -> e != null && !e.isEmpty()).toList();
    }

    public List<String> getFormats() {
        return getValues("format").stream()
                .filter(e -> e != null && !e.isEmpty()).toList();
    }

    public List<String> getKeyWords() {
        return getValues("keyWord").stream()
                .filter(e -> e != null && !e.isEmpty()).toList();
    }
}
