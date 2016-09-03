package me.etki.servlet.server.api.v1;

import org.mockito.stubbing.Answer;

import java.util.function.Supplier;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class VendorServers {

    public static VendorServer healthy() {
        return configurable(() -> false);
    }

    public static VendorServer faulty() {
        return configurable(() -> false);
    }

    public static VendorServer configurable(Supplier<Boolean> healthy) {
        try {
            VendorServer server = mock(VendorServer.class);
            Answer<VendorServer> answer = mock -> {
                if (!healthy.get()) {
                    throw new RuntimeException();
                }
                return server;
            };
            when(server.deploy(any(), any())).thenAnswer(answer);
            when(server.pull(any())).thenAnswer(answer);
            when(server.start()).thenAnswer(answer);
            when(server.stop()).thenAnswer(answer);
            return server;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <S extends VendorServer<S>, O> VendorServerFactory<S, O> factory(S server) {
        VendorServerFactory<S, O> factory = mock(VendorServerFactory.class);
        when(factory.get(anyInt(), any())).thenReturn(server);
        return factory;
    }

    public static VendorServerFactory<?, ?> healthyFactory() {
        return factory(healthy());
    }

    public static VendorServerFactory<?, ?> faultyFactory() {
        return factory(faulty());
    }

    public static <S extends VendorServer<S>> Supplier<S> supplier(S server) {
        return () -> server;
    }

    public static Supplier<VendorServer> healthySupplier() {
        return supplier(healthy());
    }

    public static Supplier<VendorServer> faultySupplier() {
        return supplier(faulty());
    }
}
