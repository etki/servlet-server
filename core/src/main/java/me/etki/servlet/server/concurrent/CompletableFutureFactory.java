package me.etki.servlet.server.concurrent;

import me.etki.servlet.server.annotation.InternalApi;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public interface CompletableFutureFactory {

    <V> CompletableFuture<V> supply(Supplier<V> supplier);

    <T, V> CompletableFuture<V> thenCompose(CompletableFuture<T> future, Function<T, CompletionStage<V>> function);
    <T, V> CompletableFuture<V> thenApply(CompletableFuture<T> future, Function<T, V> function);

    default <T> CompletableFuture<Void> thenAccept(CompletableFuture<T> future, Consumer<T> consumer) {
        return thenApply(future, value -> {
            consumer.accept(value);
            return null;
        });
    }

    // todo: rename
    @InternalApi
    default <T, V> CompletableFuture<V> thenApplyOperation(CompletableFuture<T> future, Operation<T, V> operation) {

        return thenCompose(future, value ->
                call(() -> operation.apply(value))
        );
    }

    default <T> CompletableFuture<Void> thenRun(CompletableFuture<T> future, Runnable runnable) {
        return thenApply(future, v -> {
            runnable.run();
            return null;
        });
    }

    default <V> CompletableFuture<V> thenSupply(CompletableFuture<?> future, Supplier<V> supplier) {
        return thenApply(future, v -> supplier.get());
    }

    default CompletableFuture<Void> run(Runnable task) {
        return supply(() -> {
            task.run();
            return null;
        });
    }

    default <V> CompletableFuture<V> call(Callable<V> callable) {
        CompletableFuture<V> result = new CompletableFuture<>();
        run(() -> CompletableFutures.resolve(result, callable));
        return result;
    }

    default CompletableFuture<Void> execute(Task task) {
        return call(() -> {
            task.run();
            return null;
        });
    }

    interface Task {
        void run() throws Exception;
    }

    interface Operation<T, R> {
        R apply(T value) throws Exception;
    }
}
