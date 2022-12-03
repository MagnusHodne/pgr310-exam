package no.shoppifly;

import io.micrometer.core.instrument.MeterRegistry;
import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    MeterRegistry meterRegistry;

    @Test
    public void shouldGetCarts() throws Exception {
        this.mockMvc.perform(get("/carts")).andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
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
}
