package no.shoppifly;

import io.micrometer.core.instrument.MeterRegistry;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CartServiceTest {

    @MockBean
    MeterRegistry meterRegistry;

    @Test
    public void shouldRemoveCartAfterCheckout() {

        CartService service = new NaiveCartImpl(meterRegistry);
        Cart theCart = Cart.builder().build();
        service.update(theCart);
        assertEquals(1, service.getAllCarts().size());
        String orderId = service.checkout(theCart);
        assertNotNull(orderId);

        assertEquals(0, service.getAllCarts().size());
    }

}
