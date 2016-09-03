package me.etki.servlet.server.utility;

/**
 * A stupid class that does goggles.
 *
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ContextPathNormalizer {

    /**
     * Converts arbitrary context path into library-normalized context path with leading and trailing slash.
     *
     * @param path Path to normalize
     * @return Normalized path
     */
    public static String normalize(String path) {
        // todo: remove repeated slashes
        if (!path.endsWith("/")) {
            return path + "/";
        }
        if (!path.startsWith("/")) {
            return "/" + path;
        }
        return path;
    }

    /**
     * Converts arbitrary context path into servlet API standard-conforming context path.
     *
     * @param path Path to be converted
     * @return Standard-conforming context path
     */
    public static String standardize(String path) {
        if (path.equals("/")) {
            return "";
        }
        // todo does it really match context path rules?
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return path;
    }
}
