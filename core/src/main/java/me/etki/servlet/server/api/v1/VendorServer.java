package me.etki.servlet.server.api.v1;

import me.etki.servlet.server.annotation.InternalApi;

/**
 * This interface depicts facade for vendor-specific server (e.g. tomcat, jetty, etc).
 *
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
@InternalApi
public interface VendorServer<T extends VendorServer<T>> {

    T deploy(String contextPath, Deployment deployment) throws Exception;
    T pull(String contextPath) throws Exception;
    T start() throws Exception;
    T stop() throws Exception;
}
