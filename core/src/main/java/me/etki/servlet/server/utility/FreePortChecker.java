package me.etki.servlet.server.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class FreePortChecker {

    private static final Logger LOGGER = LoggerFactory.getLogger(FreePortChecker.class);

    public static boolean check(int port) {
        try {
            new ServerSocket(port).close();
            return true;
        } catch (IOException e) {
            if (!(e instanceof BindException)) {
                LOGGER.warn("Got unexpected exception ({}): {}", e.getClass(), e.getMessage());
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("Trace: ", e);
                }
            }
            return false;
        }
    }
}
