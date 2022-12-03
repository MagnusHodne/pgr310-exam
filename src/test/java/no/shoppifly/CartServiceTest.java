package no.shoppifly;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CartServiceTest {

    //Disabled test to avoid issues with MicroMeter
    /*@Test
    public void shouldRemoveCartAfterCheckout() {

        CartService service = new NaiveCartImpl();
        Cart theCart = Cart.builder().build();
        service.update(theCart);
        assertEquals(1, service.getAllsCarts().size());
        String orderId = service.checkout(theCart);
        assertNotNull(orderId);

        assertEquals(0, service.getAllsCarts().size());
    }*/

}
