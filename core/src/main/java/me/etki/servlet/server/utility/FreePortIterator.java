package me.etki.servlet.server.utility;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Todo refactoring candidate, code is hard for reading
 *
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class FreePortIterator implements Iterator<Integer> {

    public static final int MINIMUM_PORT_NUMBER = 1;
    public static final int MAXIMUM_PORT_NUMBER = 65535;

    private final int to;

    private Integer nextPort;
    private int cursor = 0;

    public FreePortIterator(int from, int to) {
        if (from < MINIMUM_PORT_NUMBER) {
            throw new IllegalArgumentException("`from` port can't be less than " + MINIMUM_PORT_NUMBER);
        }
        if (to > MAXIMUM_PORT_NUMBER) {
            throw new IllegalArgumentException("`to` port can't be more than " + MAXIMUM_PORT_NUMBER);
        }
        if (from > to) {
            throw new IllegalArgumentException("`from` port is bigger than `to` port");
        }
        this.cursor = from - 1;
        this.to = to;
    }

    @Override
    public synchronized boolean hasNext() {
        if (nextPort != null) {
            return true;
        }
        while (++cursor <= to) {
            if (FreePortChecker.check(cursor)) {
                nextPort = cursor;
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized Integer next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        Integer buffer = nextPort;
        nextPort = null;
        return buffer;
    }
}
