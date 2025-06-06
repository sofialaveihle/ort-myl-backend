package ar.com.mylback.utils.url;

public class PathHelper {

    /**
     * Extracts the last segment from a URI path.
     */
    public static String getLastPathSegment(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }

        String[] segments = path.split("/");
        // Loop from the end to skip empty segments
        for (int i = segments.length - 1; i >= 0; i--) {
            if (!segments[i].isEmpty()) {
                return segments[i];
            }
        }
        return null;
    }
}
