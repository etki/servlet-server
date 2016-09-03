package me.etki.servlet.server.api.v1;

import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class Deployment {

    private HttpServlet servlet;
    private List<Filter> filters = Collections.emptyList();
    private Path resourceRoot;
    private Path workingDirectory;
    private Duration deploymentTimeout;
    private Duration pullTimeout;

    public HttpServlet getServlet() {
        return servlet;
    }

    public Deployment setServlet(HttpServlet servlet) {
        this.servlet = servlet;
        return this;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public Deployment setFilters(List<Filter> filters) {
        this.filters = filters;
        return this;
    }

    public Path getResourceRoot() {
        return resourceRoot;
    }

    public Deployment setResourceRoot(Path resourceRoot) {
        this.resourceRoot = resourceRoot;
        return this;
    }

    public Path getWorkingDirectory() {
        return workingDirectory;
    }

    public Deployment setWorkingDirectory(Path workingDirectory) {
        this.workingDirectory = workingDirectory;
        return this;
    }

    public Duration getDeploymentTimeout() {
        return deploymentTimeout;
    }

    public Deployment setDeploymentTimeout(Duration deploymentTimeout) {
        this.deploymentTimeout = deploymentTimeout;
        return this;
    }

    public Duration getPullTimeout() {
        return pullTimeout;
    }

    public Deployment setPullTimeout(Duration pullTimeout) {
        this.pullTimeout = pullTimeout;
        return this;
    }

    public static Deployment of(HttpServlet servlet) {
        return new Deployment().setServlet(servlet);
    }
}
