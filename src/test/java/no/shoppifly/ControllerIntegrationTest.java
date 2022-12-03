package no.shoppifly;

import io.micrometer.core.instrument.MeterRegistry;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    MeterRegistry meterRegistry;

    @Test
    @Order(1)
    public void shouldGetCarts() throws Exception {
        this.mockMvc.perform(get("/carts")).andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    @Order(2)
    public void shouldPostCart() throws Exception {
        var payload = new JSONObject()
                .put("items", new JSONArray()
                        .put(new JSONObject()
                                .put("description", "Test item")
                                .put("qty", 1)
                                .put("unitPrice", 400)));

        this.mockMvc.perform(post("/cart")
                        .content(payload.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(payload.toString()));
    }

    @Test
    @Order(3)
    public void shouldPostAndCheckoutCart() throws Exception {
        var initialNumCarts = new JSONArray(this.mockMvc.perform(get("/carts")).andReturn().getResponse().getContentAsString()).length();

        var payload = new JSONObject()
                .put("items", new JSONArray()
                        .put(new JSONObject()
                                .put("description", "Test item")
                                .put("qty", 1)
                                .put("unitPrice", 400)));

        var responseContents = this.mockMvc.perform(post("/cart")
                        .content(payload.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(payload.toString())).andReturn().getResponse().getContentAsString();

        this.mockMvc.perform(get("/carts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(initialNumCarts +1));

        var jsonPayload = new JSONObject(responseContents);
        assertTrue(jsonPayload.has("id"));
        var uuid = jsonPayload.get("id");

        this.mockMvc.perform(post("/cart/checkout")
                .content(new JSONObject().put("items", new JSONArray()).put("id", uuid).toString())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        var postCheckoutNumCarts = new JSONArray(this.mockMvc.perform(get("/carts")).andReturn().getResponse().getContentAsString()).length();

        assertEquals(initialNumCarts, postCheckoutNumCarts);


    }
}
