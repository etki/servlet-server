package me.etki.servlet.server.concurrent;

import java.util.concurrent.ForkJoinPool;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class CompletableFutureFactories {

    public static CompletableFutureFactory fromForkJoinPool() {
        return new BasicCompletableFutureFactory(ForkJoinPool.commonPool());
    }

    public static CompletableFutureFactory fromCurrentThread() {
        return new BasicCompletableFutureFactory(Runnable::run);
    }
}
