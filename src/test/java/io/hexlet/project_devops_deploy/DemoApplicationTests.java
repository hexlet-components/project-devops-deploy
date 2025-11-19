package io.hexlet.project_devops_deploy;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.project_devops_deploy.dto.BulletinDto;
import io.hexlet.project_devops_deploy.dto.BulletinRequest;
import io.hexlet.project_devops_deploy.model.BulletinState;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    void bulletinCrudFlow() throws Exception {
        BulletinRequest createRequest = BulletinRequest.builder()
                .title("Test title")
                .description("Test description")
                .state(BulletinState.DRAFT)
                .contact("contact@example.com")
                .build();

        MvcResult createResult = mockMvc.perform(post("/bulletins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test title"))
                .andExpect(jsonPath("$.state").value("DRAFT"))
                .andReturn();

        BulletinDto created = objectMapper.readValue(createResult.getResponse().getContentAsString(), BulletinDto.class);

        mockMvc.perform(get("/bulletins/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(created.getId()))
                .andExpect(jsonPath("$.title").value("Test title"));

        BulletinRequest updateRequest = BulletinRequest.builder()
                .title("Updated title")
                .description("Updated description")
                .state(BulletinState.PUBLISHED)
                .contact("contact@example.com")
                .build();

        mockMvc.perform(put("/bulletins/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated title"))
                .andExpect(jsonPath("$.state").value("PUBLISHED"));

        mockMvc.perform(get("/bulletins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(delete("/bulletins/" + created.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/bulletins/" + created.getId()))
                .andExpect(status().isNotFound());
    }
}
