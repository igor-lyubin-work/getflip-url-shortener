package com.getflip.urlshortener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getflip.urlshortener.dto.ShortenUrlRequest;
import com.getflip.urlshortener.service.url.UrlServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
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
    public void shortenUrl_shouldReturnCreatedAndShortUrl() throws Exception {
        String originalUrl = "https://www.getflip.com";
        String alias = "abc12345";
        ShortenUrlRequest request = new ShortenUrlRequest(originalUrl);

        when(urlService.shortenUrl(originalUrl)).thenReturn(alias);

        mockMvc.perform(post(UrlController.BASE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.longUrl").value(originalUrl))
                .andExpect(jsonPath("$.shortUrl", containsString(alias)));
    }

    @Test
    public void redirectUrl_shouldReturnFound_whenAliasExists() throws Exception {
        String alias = "abc12345";
        String originalUrl = "https://www.getflip.com";
        when(urlService.getOriginalUrl(alias)).thenReturn(originalUrl);

        mockMvc.perform(get(UrlController.BASE_PATH)
                        .param("alias", alias))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", originalUrl));
    }

    @Test
    public void redirectUrl_shouldReturnNotFound_whenAliasDoesNotExist() throws Exception {
        when(urlService.getOriginalUrl("unknown")).thenReturn(null);

        mockMvc.perform(get(UrlController.BASE_PATH)
                        .param("alias", "unknown"))
                .andExpect(status().isNotFound());
    }
}
