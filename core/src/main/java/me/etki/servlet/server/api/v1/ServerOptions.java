package me.etki.servlet.server.api.v1;

import java.time.Duration;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class ServerOptions {

    private int minimumThreads;
    private int maximumThreads;
    private Duration defaultDeploymentTimeout;
    private Duration defaultPullTimeout;

    public int getMinimumThreads() {
        return minimumThreads;
    }

    public ServerOptions setMinimumThreads(int minimumThreads) {
        this.minimumThreads = minimumThreads;
        return this;
    }

    public int getMaximumThreads() {
        return maximumThreads;
    }

    public ServerOptions setMaximumThreads(int maximumThreads) {
        this.maximumThreads = maximumThreads;
        return this;
    }

    public Duration getDefaultDeploymentTimeout() {
        return defaultDeploymentTimeout;
    }

    public ServerOptions setDefaultDeploymentTimeout(Duration defaultDeploymentTimeout) {
        this.defaultDeploymentTimeout = defaultDeploymentTimeout;
        return this;
    }

    public Duration getDefaultPullTimeout() {
        return defaultPullTimeout;
    }

    public ServerOptions setDefaultPullTimeout(Duration defaultPullTimeout) {
        this.defaultPullTimeout = defaultPullTimeout;
        return this;
    }
}
