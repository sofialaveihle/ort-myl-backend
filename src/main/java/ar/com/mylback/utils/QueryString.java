package ar.com.mylback.utils;

import jakarta.validation.constraints.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<String> getValues(String key) {
        List<String> values = query.get(key);
        if (values == null) {
            values = new ArrayList<>();
        }
        return values;
    }

    public int getPage() {
        List<String> values = getValues("page");
        if (values == null || values.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(values.get(0));
    }

    public int getPageSize() {
        List<String> values = getValues("pageSize");
        if (values == null || values.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(values.get(0));
    }
}
