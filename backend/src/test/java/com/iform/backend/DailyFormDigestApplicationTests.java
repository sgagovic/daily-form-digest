package com.iform.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class DailyFormDigestApplicationTests {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void validEntryWithoutOptionalFieldsIsValidatedButNotSaved() throws Exception {
        mvc.perform(post("/api/entries").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Alex", "email":"alex@example.com", "message":"Hello"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.validated").value(true))
                .andExpect(jsonPath("$.saved").value(false));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void emptyAndSpacesOnlyRequiredFieldsProduceTheSameErrors(String value) throws Exception {
        String body = """
                {"name":"%s", "email":"alex@example.com", "message":"%s"}
                """.formatted(value, value);
        mvc.perform(post("/api/entries").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").value("Please enter your name."))
                .andExpect(jsonPath("$.errors.message").value("Please enter a message."));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "not-an-email"})
    void invalidEmailIsRejected(String email) throws Exception {
        mvc.perform(post("/api/entries").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Alex", "email":"%s", "message":"Hello"}
                                """.formatted(email)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").isString());
    }

    @Test
    void missingRequiredFieldsAreRejected() throws Exception {
        mvc.perform(post("/api/entries").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.email").exists())
                .andExpect(jsonPath("$.errors.message").exists());
    }

    @Test
    void oversizedMessageIsRejected() throws Exception {
        mvc.perform(post("/api/entries").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Alex", "email":"alex@example.com", "message":"%s"}
                                """.formatted("x".repeat(5001))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.message").exists());
    }

    @ParameterizedTest
    @ValueSource(strings = {"website-1", "website-2", "direct"})
    void knownWebsiteSourcesAndBlankCompanyAreAccepted(String source) throws Exception {
        mvc.perform(post("/api/entries").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Alex", "email":"alex@example.com", "company":"", "message":"Hello", "source":"%s"}
                                """.formatted(source)))
                .andExpect(status().isOk());
    }

    @Test
    void unknownSourceIsRejected() throws Exception {
        mvc.perform(post("/api/entries").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Alex", "email":"alex@example.com", "message":"Hello", "source":"unknown"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.source").exists());
    }

    @Test
    void malformedJsonReturnsSafeError() throws Exception {
        mvc.perform(post("/api/entries").contentType(MediaType.APPLICATION_JSON).content("{"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Send a valid JSON request body."));
    }
}
