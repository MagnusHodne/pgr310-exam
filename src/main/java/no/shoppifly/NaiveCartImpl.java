package no.shoppifly;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
class NaiveCartImpl implements CartService {

    private final Map<String, Cart> shoppingCarts = new HashMap<>();
    private final MeterRegistry meterRegistry;

    @Autowired
    public NaiveCartImpl(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public Cart getCart(String id) {
        return shoppingCarts.get(id);
    }

    @Override
    public Cart update(Cart cart) {
        if (cart.getId() == null) {
            meterRegistry.counter("carts.count").increment();
            cart.setId(UUID.randomUUID().toString());
        }
        shoppingCarts.put(cart.getId(), cart);
        return shoppingCarts.put(cart.getId(), cart);
    }

    @Override
    public String checkout(Cart cart) {
        shoppingCarts.remove(cart.getId());
        meterRegistry.counter("carts.count").increment(-1);
        return UUID.randomUUID().toString();
    }

    @Override
    public List<String> getAllsCarts() {
        return new ArrayList<>(shoppingCarts.keySet());
    }

    // @author Jim; I'm so proud of this one, took me one week to figure out !!!
    public float total() {
        return shoppingCarts.values().stream()
                .flatMap(c -> c.getItems().stream()
                        .map(i -> i.getUnitPrice() * i.getQty()))
                .reduce(0f, Float::sum);
    }
}
