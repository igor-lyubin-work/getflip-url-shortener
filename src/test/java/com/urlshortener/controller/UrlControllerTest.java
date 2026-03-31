package com.urlshortener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getflip.urlshortener.controller.UrlController;
import com.getflip.urlshortener.dto.ShortenUrlRequest;
import com.getflip.urlshortener.service.UrlServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UrlController.class)
class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlServiceImpl urlService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shortenUrl_shouldReturnCreated() throws Exception {
        ShortenUrlRequest request = new ShortenUrlRequest("https://example.com");
        when(urlService.shortenUrl(anyString())).thenReturn("abc123");

        mockMvc.perform(post("/api/v1/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortUrl").value("http://localhost/abc123"))
                .andExpect(jsonPath("$.originalUrl").value("https://example.com"));
    }

    @Test
    void shortenUrl_shouldReturnBadRequest_whenInvalidInput() throws Exception {
        ShortenUrlRequest request = new ShortenUrlRequest("");

        mockMvc.perform(post("/api/v1/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void redirectUrl_shouldReturnFound() throws Exception {
        when(urlService.getOriginalUrl("abc123")).thenReturn("https://example.com");

        mockMvc.perform(get("/api/v1/urls")
                        .param("alias", "abc123"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "https://example.com"));
    }

    @Test
    void redirectUrl_shouldReturnNotFound_whenAliasMissing() throws Exception {
        when(urlService.getOriginalUrl("abc123")).thenReturn(null);

        mockMvc.perform(get("/api/v1/urls")
                        .param("alias", "abc123"))
                .andExpect(status().isNotFound());
    }

    @Test
    void redirectUrl_shouldReturnBadRequest_whenAliasNotProvided() throws Exception {
        mockMvc.perform(get("/api/v1/urls"))
                .andExpect(status().isBadRequest());
    }
}
