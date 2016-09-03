package me.etki.servlet.server.api.v1;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface VendorServerFactory<S extends VendorServer, O> {
    S get(int port, O options);
}
