package me.etki.servlet.server.api.v1;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * This is interface represents a server that would be managed by library end user.
 *
 * todo: graceful pull - servlet should have a chance for a graceful shutdown if it implements special interface / it's
 * deployment register corresponding hooks
 *
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface ServletServer {

    /**
     * Performs servlet deployment, resolves when servlet has been fully deployed. This method can be safely called
     * before server start / full server start, but returned CompletableFuture won't resolve until server has fully
     * started and servlet has been actually deployed to the server.
     *
     * @param contextPath Context path for deployment
     * @param deployment Servlet to be deployed alongside metadata
     * @return CF'd operation success: true if deploy was successful, false if context path has been occupied before
     * deployment, exception in case of deployment error and other cases (should there be any?).
     */
    CompletableFuture<Boolean> deploy(String contextPath, Deployment deployment);

    /**
     * Pulls active deployment down. Currently there is no mechanism to notify servlet that it should halt processing.
     *
     * @param contextPath Context path of deployment
     * @return CF'd operation result: null if there was no deployment under specified context path, particular
     * deployment if it existed, exception in case of exception (pun intended).
     */
    CompletableFuture<Deployment> pull(String contextPath);

    boolean deployed(String contextPath);
    Map<String, Deployment> listDeployments();

    CompletableFuture<Void> start();
    CompletableFuture<Void> shutdown();
    CompletableFuture<Void> awaitStart();
    CompletableFuture<Void> awaitShutdown();
}
