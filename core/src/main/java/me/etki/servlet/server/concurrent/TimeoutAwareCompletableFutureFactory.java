package me.etki.servlet.server.concurrent;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface TimeoutAwareCompletableFutureFactory extends CompletableFutureFactory {

    /**
     * Installs cancellable timeout over completable future.
     *
     * @param future Future that need timeout to be installed.
     * @param timeout Timeout duration value.
     * @param unit Timeout duration unit.
     * @param <V> Expected CF value.
     * @return CompletableFuture representing timeout that may be cancelled as well.
     */
    <V> CompletableFuture<Void> setTimeout(CompletableFuture<V> future, long timeout, TimeUnit unit);

    default <V> CompletableFuture<Void> setTimeout(CompletableFuture<V> future, Duration duration) {
        return setTimeout(future, duration.toNanos(), TimeUnit.NANOSECONDS);
    }

    default <V> CompletableFuture<V> supply(Supplier<V> supplier, long timeout, TimeUnit unit) {
        CompletableFuture<V> future = supply(supplier);
        setTimeout(future, timeout, unit);
        return future;
    }

    default CompletableFuture<Void> run(Runnable runnable, long timeout, TimeUnit unit) {
        CompletableFuture<Void> future = run(runnable);
        setTimeout(future, timeout, unit);
        return future;
    }

    default <V> CompletableFuture<V> call(Callable<V> callable, long timeout, TimeUnit unit) {
        CompletableFuture<V> future = call(callable);
        setTimeout(future, timeout, unit);
        return future;
    }

    default CompletableFuture<Void> execute(Task task, long timeout, TimeUnit unit) {
        CompletableFuture<Void> future = execute(task);
        setTimeout(future, timeout, unit);
        return future;
    }

    default <V> CompletableFuture<V> supply(Supplier<V> supplier, Duration duration) {
        return supply(supplier, duration.toNanos(), TimeUnit.NANOSECONDS);
    }

    default CompletableFuture<Void> run(Runnable runnable, Duration duration) {
        return run(runnable, duration.toNanos(), TimeUnit.NANOSECONDS);
    }

    default <V> CompletableFuture<V> call(Callable<V> callable, Duration duration) {
        return call(callable, duration.toNanos(), TimeUnit.NANOSECONDS);
    }

    default CompletableFuture<Void> execute(Task task, Duration duration) {
        return execute(task, duration.toNanos(), TimeUnit.NANOSECONDS);
    }
}
