package me.etki.servlet.server.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class BasicCompletableFutureFactory implements CompletableFutureFactory {

    private final Executor executor;

    public BasicCompletableFutureFactory(Executor executor) {
        this.executor = executor;
    }

    @Override
    public <T> CompletableFuture<T> supply(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, executor);
    }

    @Override
    public CompletableFuture<Void> run(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, executor);
    }

    @Override
    public <T, V> CompletableFuture<V> thenCompose(
            CompletableFuture<T> future,
            Function<T, CompletionStage<V>> function) {

        return future.thenComposeAsync(function, executor);
    }

    @Override
    public <T, V> CompletableFuture<V> thenApply(CompletableFuture<T> future, Function<T, V> function) {
        return future.thenApplyAsync(function, executor);
    }
}
