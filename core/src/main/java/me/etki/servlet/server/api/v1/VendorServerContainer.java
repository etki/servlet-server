package me.etki.servlet.server.api.v1;

import me.etki.servlet.server.concurrent.CompletableFutureFactory;
import me.etki.servlet.server.concurrent.CompletableFutures;
import me.etki.servlet.server.exception.ShutdownServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class VendorServerContainer {

    private static final Logger LOGGER = LoggerFactory.getLogger(VendorServerContainer.class);

    private final AtomicReference<ContainerState> state
            = new AtomicReference<>(ContainerState.down(CompletableFutures.completed(null)));

    private final Supplier<VendorServer> serverFactory;
    private final CompletableFutureFactory completableFutureFactory;

    public VendorServerContainer(
            Supplier<VendorServer> serverFactory,
            CompletableFutureFactory completableFutureFactory) {

        this.serverFactory = serverFactory;
        this.completableFutureFactory = completableFutureFactory;
    }

    private CompletableFuture<VendorServer> startInternal() {
        CompletableFuture<VendorServer> hook = state.get().getNextStateTransitionHook();
        ContainerState currentState;
        do {
            currentState = state.get();
            if (currentState.getTargetState().equals(ServerState.UP)) {
                return currentState.getTransitionHook().thenApply(v -> null);
            }
            if (!currentState.getTransitionHook().isDone()) {
                return CompletableFutures.completed(null);
            }
            // if method hasn't returned any value, server is currently shut down and no transition is going on
            // so let's try to swap current state, and, if somebody got there first, try again
        } while (!state.compareAndSet(currentState, ContainerState.up(hook)));
        // so, current execution thread is in control
        ContainerState pinnedState = currentState;
        return completableFutureFactory
                .call(() -> {
                    VendorServer server = serverFactory.get();
                    LOGGER.debug("Starting server {}", server);
                    try {
                        server.start();
                    } catch (Throwable error) {
                        LOGGER.debug("Failed to start server {}, returning previous state and propagating error",
                                server);
                        state.set(pinnedState);
                        throw error;
                    }
                    LOGGER.debug("Successfully started server {}", server);
                    pinnedState.getNextStateTransitionHook().complete(server);
                    return server;
                });
    }

    private CompletableFuture<VendorServer> stopInternal() {
        CompletableFuture<VendorServer> hook = state.get().getNextStateTransitionHook();
        ContainerState currentState;
        do {
            currentState = state.get();
            if (currentState.getTargetState().equals(ServerState.DOWN)) {
                return currentState.getTransitionHook().thenApply(v -> null);
            }
            if (!currentState.getTransitionHook().isDone()) {
                return CompletableFuture.completedFuture(null);
            }
            // at this moment it is clear that server is in UP state
            // let's try to swap state with new transition, and, if somebody has taken action after reads above,
            // try everything again
        } while (!state.compareAndSet(currentState, ContainerState.down(hook)));
        // again, current transition is governed by this very execution thread
        ContainerState pinnedState = currentState;
        return completableFutureFactory
                .thenApplyOperation(currentState.getTransitionHook(), server -> {
                    LOGGER.debug("Stopping server {}", server);
                    try {
                        server.stop();
                    } catch (Throwable error) {
                        LOGGER.debug("Failed to stop server {}, returning previous state and propagating error",
                                server);
                        state.set(pinnedState);
                        throw error;
                    }
                    LOGGER.debug("Successfully stopped server {}", server);
                    pinnedState.getNextStateTransitionHook().complete(server);
                    return server;
                });
    }

    public CompletableFuture<Boolean> start() {
        return startInternal().thenApply(server -> server != null);
    }

    public CompletableFuture<Boolean> stop() {
        return stopInternal().thenApply(server -> server != null);
    }

    public CompletableFuture<Void> getStartHook() {
        // todo: this code generates new CF every call, while this is completely unnecessary
        ContainerState currentState = state.get();
        return currentState.getTransitionHook()
                .thenCompose(v -> {
                    if (currentState.getTargetState().equals(ServerState.UP)) {
                        return currentState.getTransitionHook();
                    }
                    return currentState.getNextStateTransitionHook();
                })
                .thenApply(v -> null);
    }

    public CompletableFuture<Void> getStopHook() {
        ContainerState currentState = state.get();
        return currentState.getTransitionHook()
                .thenCompose(v -> {
                    if (currentState.getTargetState().equals(ServerState.DOWN)) {
                        return currentState.getTransitionHook();
                    }
                    return currentState.getNextStateTransitionHook();
                })
                .thenApply(v -> null);
    }

    // todo [IMPORTANT] this and following method are not concurrency-safe
    // it is possible that state has changed before transition hook dependent has been run, so actions are taken on
    // possibly stale state
    // while it will work perfectly in most cases, it looks like whole class should be reimplemented using more coarse
    // synchronization
    public CompletableFuture<Void> deploy(String contextPath, Deployment deployment) {
        ContainerState currentState = state.get();
        return completableFutureFactory
                .thenCompose(currentState.getTransitionHook(), server -> {
                    if (currentState.getTargetState().equals(ServerState.DOWN)) {
                        return CompletableFutures.exceptional(new ShutdownServerException());
                    }
                    try {
                        server.deploy(contextPath, deployment);
                        return CompletableFutures.completed(null);
                    } catch (Throwable error) {
                        return CompletableFutures.exceptional(error);
                    }
                });
    }

    public CompletableFuture<Void> pull(String contextPath) {
        ContainerState currentState = state.get();
        return completableFutureFactory
                .thenCompose(currentState.getTransitionHook(), server -> {
                    if (currentState.getTargetState().equals(ServerState.DOWN)) {
                        return CompletableFutures.exceptional(new ShutdownServerException());
                    }
                    try {
                        server.pull(contextPath);
                        return CompletableFutures.completed(null);
                    } catch (Throwable error) {
                        return CompletableFutures.exceptional(error);
                    }
                });
    }

    private static class ContainerState {

        private final CompletableFuture<VendorServer> nextStateTransitionHook = new CompletableFuture<>();

        private final ServerState targetState;
        private final CompletableFuture<VendorServer> transitionHook;

        public ContainerState(ServerState targetState, CompletableFuture<VendorServer> transitionHook) {
            this.targetState = targetState;
            this.transitionHook = transitionHook;
        }

        public ServerState getTargetState() {
            return targetState;
        }

        public CompletableFuture<VendorServer> getTransitionHook() {
            return transitionHook;
        }

        public CompletableFuture<VendorServer> getNextStateTransitionHook() {
            return nextStateTransitionHook;
        }

        public static ContainerState up(CompletableFuture<VendorServer> transitionHook) {
            return new ContainerState(ServerState.UP, transitionHook);
        }

        public static ContainerState down(CompletableFuture<VendorServer> transitionHook) {
            return new ContainerState(ServerState.DOWN, transitionHook);
        }
    }

    private enum ServerState {
        UP,
        DOWN
    }
}
