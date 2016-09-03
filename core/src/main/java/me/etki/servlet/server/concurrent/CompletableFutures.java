package me.etki.servlet.server.concurrent;

import me.etki.servlet.server.annotation.Experimental;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class CompletableFutures {

    public static <V> CompletableFuture<V> completed(V value) {
        return CompletableFuture.completedFuture(value);
    }

    public static <V> CompletableFuture<V> exceptional(Throwable error) {
        CompletableFuture<V> future = new CompletableFuture<>();
        future.completeExceptionally(error);
        return future;
    }

    public static <V> CompletableFuture<V> of(V value, Throwable error) {
        return finish(new CompletableFuture<>(), value, error);
    }

    public static <V> CompletableFuture<V> finish(CompletableFuture<V> future, V value, Throwable error) {
        if (error == null) {
            future.complete(value);
            return future;
        }
        if (value == null) {
            future.completeExceptionally(error);
            return future;
        }
        throw new IllegalArgumentException("Value and error can't both be not null");
    }

    public static <V> CompletableFuture<V> pipe(CompletableFuture<V> source, CompletableFuture<V> target) {
        source.thenApply(target::complete).exceptionally(target::completeExceptionally);
        return target;
    }

    @Experimental
    public static <V> Supplier<CompletableFuture<V>> wrap(Callable<V> callable) {
        return () -> execute(callable);
    }

    @Experimental
    public static <V> CompletableFuture<V> execute(Callable<V> callable) {
        return resolve(new CompletableFuture<>(), callable);
    }

    @Experimental
    public static <V> CompletableFuture<V> resolve(CompletableFuture<V> future, Callable<V> callable) {
        try {
            future.complete(callable.call());
        } catch (Throwable error) {
            future.completeExceptionally(error);
        }
        return future;
    }
}
