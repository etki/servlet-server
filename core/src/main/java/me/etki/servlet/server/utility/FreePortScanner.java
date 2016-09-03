package me.etki.servlet.server.utility;

import java.util.Iterator;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class FreePortScanner {

    public static final int FIRST_PUBLIC_PORT = 1024;
    public static final int FIRST_IANA_EPHEMERAL_PORT = 49152;

    public static Iterator<Integer> getFreePortIterator() {
        return getFreePortIterator(FIRST_PUBLIC_PORT);
    }

    public static Iterator<Integer> getFreePortIterator(int from) {
        return getFreePortIterator(from, FIRST_IANA_EPHEMERAL_PORT - 1);
    }

    public static Iterator<Integer> getFreePortIterator(int from, int to) {
        return new FreePortIterator(from, to);
    }

    public static int getFreePort() {
        return getFreePort(FIRST_PUBLIC_PORT);
    }

    public static int getFreePort(int from) {
        return getFreePort(from, FIRST_IANA_EPHEMERAL_PORT - 1);
    }

    public static int getFreePort(int from, int to) {
        Iterator<Integer> iterator = getFreePortIterator(from, to);
        if (!iterator.hasNext()) {
            return -1;
        }
        return iterator.next();
    }
}
