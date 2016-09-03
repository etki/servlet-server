package me.etki.servlet.server.api.v1;

import me.etki.servlet.server.concurrent.CompletableFutureFactories;
import me.etki.servlet.server.concurrent.CompletableFutureFactory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class VendorServerContainerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(VendorServerContainerTest.class);

    private CompletableFutureFactory currentThreadBasedFactory;
    private AtomicBoolean healthControl;
    private Supplier<VendorServer> supplier;

    @Before
    public void setUp() {
        currentThreadBasedFactory = CompletableFutureFactories.fromCurrentThread();
        healthControl = new AtomicBoolean(true);
        supplier = () -> VendorServers.configurable(healthControl::get);
    }


    @Test
    public void shouldCorrectlyReturnResultValues() throws Exception {
        VendorServerContainer container = new VendorServerContainer(supplier, currentThreadBasedFactory);
        assertFalse(container.stop().get());
        assertTrue(container.start().get());
        assertFalse(container.start().get());
        assertTrue(container.stop().get());
        assertFalse(container.stop().get());
    }

    @Test
    public void shouldCorrectlyPropagateStartStopErrors() throws Exception {
        VendorServerContainer container = new VendorServerContainer(supplier, currentThreadBasedFactory);
        healthControl.set(false);
        CompletableFuture<Boolean> startAction = container.start();
        startAction.exceptionally(e -> null).get();
        assertTrue(startAction.isCompletedExceptionally());
        healthControl.set(true);
        container.start().get();
        healthControl.set(false);
        CompletableFuture<Boolean> stopAction = container.stop();
        stopAction.exceptionally(e -> null).get();
        assertTrue(stopAction.isCompletedExceptionally());
    }

    @Test
    public void shouldExecuteStartHookOnStart() throws Exception {
        VendorServerContainer container = new VendorServerContainer(supplier, currentThreadBasedFactory);
        CompletableFuture<Void> startHook = container.getStartHook();
        assertFalse(startHook.isDone());
        container.start().get();
        assertTrue(startHook.isDone());
        assertFalse(startHook.isCompletedExceptionally());
    }

    @Test
    public void shouldNotExecuteStartHookOnFailedStart() throws Exception {
        VendorServerContainer container = new VendorServerContainer(supplier, currentThreadBasedFactory);
        CompletableFuture<Void> startHook = container.getStartHook();
        assertFalse(startHook.isDone());
        healthControl.set(false);
        CompletableFuture<Boolean> startAction = container.start();
        startAction.exceptionally(e -> null).get();
        assertTrue(startAction.isCompletedExceptionally());
        assertFalse(startHook.isDone());
    }

    @Test
    public void shouldExecuteStopHookOnStop() throws Exception {
        VendorServerContainer container = new VendorServerContainer(supplier, currentThreadBasedFactory);
        container.start().get();
        CompletableFuture<Void> stopHook = container.getStopHook();
        assertFalse(stopHook.isDone());
        container.stop().get();
        assertTrue(stopHook.isDone());
        assertFalse(stopHook.isCompletedExceptionally());
    }

    @Test
    public void shouldNotExecuteStopHookOnFailedStop() throws Exception {
        VendorServerContainer container = new VendorServerContainer(supplier, currentThreadBasedFactory);
        container.start().get();
        CompletableFuture<Void> stopHook = container.getStopHook();
        assertFalse(stopHook.isDone());
        healthControl.set(false);
        CompletableFuture<Boolean> stopAction = container.stop();
        stopAction.exceptionally(e -> null).get();
        assertTrue(stopAction.isCompletedExceptionally());
        assertFalse(stopHook.isDone());
    }
}