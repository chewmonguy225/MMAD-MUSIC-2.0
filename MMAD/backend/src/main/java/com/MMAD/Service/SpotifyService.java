package com.MMAD.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.MMAD.dto.item.AlbumDTO;
import com.MMAD.dto.item.ArtistDTO;
import com.MMAD.dto.item.ItemDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SpotifyService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final String SEARCH_URL = "https://api.spotify.com/v1/search";

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    private String accessToken;
    private long tokenTimestamp;

    public SpotifyService() {}

    // =========================
    // TOKEN HANDLING
    // =========================

    private String getAccessToken() {
        if (accessToken == null || isTokenExpired()) {
            accessToken = retrieveAccessToken();
            tokenTimestamp = System.currentTimeMillis();
        }
        return accessToken;
    }

    private boolean isTokenExpired() {
        return System.currentTimeMillis() - tokenTimestamp > 3600_000;
    }

    private String retrieveAccessToken() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String auth = clientId + ":" + clientSecret;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

            headers.set("Authorization", "Basic " + encodedAuth);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "client_credentials");

            HttpEntity<MultiValueMap<String, String>> request =
                    new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    TOKEN_URL,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            JsonNode json = objectMapper.readTree(response.getBody());
            return json.path("access_token").asText();

        } catch (Exception e) {
            throw new RuntimeException("Failed to get Spotify token", e);
        }
    }

    // =========================
    // PUBLIC SEARCH (FIXED)
    // =========================

    public List<ItemDTO> searchSpotify(String query, List<String> types) {

        JsonNode json = callSpotify(query, types);

        List<ItemDTO> results = new ArrayList<>();

        if (types.contains("artist")) {
            for (JsonNode node : json.path("artists").path("items")) {
                ArtistDTO dto = mapArtist(node);
                dto.setRelevance(calculateRelevance(node, query, "artist"));
                results.add(dto);
            }
        }

        if (types.contains("album")) {
            for (JsonNode node : json.path("albums").path("items")) {
                AlbumDTO dto = mapAlbum(node);
                dto.setRelevance(calculateRelevance(node, query, "album"));
                results.add(dto);
            }
        }

        // ✅ TRUE GLOBAL RELEVANCE SORT
        results.sort(Comparator.comparingInt(ItemDTO::getRelevance).reversed());

        return results;
    }

    // =========================
    // RELEVANCE ENGINE (REAL FIX)
    // =========================

    private int calculateRelevance(JsonNode node, String query, String type) {

        String name = node.path("name").asText("").toLowerCase();
        String q = query.toLowerCase();

        int score = 0;

        // type boost
        if ("artist".equals(type)) score += 2000;
        if ("album".equals(type)) score += 1500;

        // exact match
        if (name.equals(q)) score += 5000;

        // starts with query
        if (name.startsWith(q)) score += 3000;

        // contains query
        if (name.contains(q)) score += 1000;

        // Spotify popularity boost (if exists)
        if (node.has("popularity")) {
            score += node.path("popularity").asInt();
        }

        return score;
    }

    // =========================
    // SPOTIFY API CALL
    // =========================

    private JsonNode callSpotify(String query, List<String> types) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(getAccessToken());
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            String url = UriComponentsBuilder.fromUriString(SEARCH_URL)
                    .queryParam("q", query)
                    .queryParam("type", String.join(",", types))
                    .build()
                    .toUriString();

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class
            );

            return objectMapper.readTree(response.getBody());

        } catch (Exception e) {
            throw new RuntimeException("Spotify API call failed", e);
        }
    }

    // =========================
    // MAPPERS
    // =========================

    private ArtistDTO mapArtist(JsonNode node) {
        return new ArtistDTO(
                null,
                node.path("id").asText(),
                node.path("name").asText(),
                extractImage(node)
        );
    }

    private AlbumDTO mapAlbum(JsonNode node) {

        List<ArtistDTO> artists = new ArrayList<>();

        for (JsonNode artist : node.path("artists")) {
            artists.add(new ArtistDTO(
                    null,
                    artist.path("id").asText(),
                    artist.path("name").asText(),
                    "default"
            ));
        }

        return new AlbumDTO(
                null,
                node.path("id").asText(),
                node.path("name").asText(),
                extractImage(node),
                artists
        );
    }

    // =========================
    // IMAGE HELPER
    // =========================

    private String extractImage(JsonNode node) {

        JsonNode images = node.path("images");

        if (images.isArray() && !images.isEmpty()) {
            return images.get(0).path("url").asText();
        }

        return "default";
    }
}