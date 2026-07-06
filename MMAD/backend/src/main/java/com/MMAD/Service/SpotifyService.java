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

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    TOKEN_URL,
                    HttpMethod.POST,
                    request,
                    String.class);

            JsonNode json = objectMapper.readTree(response.getBody());
            return json.path("access_token").asText();

        } catch (Exception e) {
            throw new RuntimeException("Failed to get Spotify token", e);
        }
    }

    // =========================
    // SEARCH
    // =========================

    public List<ItemDTO> searchSpotify(String query, List<String> types) {

        JsonNode json = callSpotify(query, types);

        List<ItemDTO> results = new ArrayList<>();

        if (types.contains("artist")) {
            for (JsonNode node : json.path("artists").path("items")) {
                ArtistDTO dto = mapArtist(node);
                results.add(dto);
            }
        }

        if (types.contains("album")) {
        for (JsonNode node : json.path("albums").path("items")) {
        AlbumDTO dto = mapAlbum(node);
        results.add(dto);
        }
        }

        results.forEach(item -> item.setRelevance(scoreItem(query, item)));
        return results;
    }

    private int scoreItem(String query, ItemDTO item) {

        String q = normalize(query);
        String name = normalize(item.getName());
    
        int score = 0;
    
        // -------------------
        // EXACT MATCH
        // -------------------
        if (name.equals(q)) {
            return 1_000_000;
        }
    
        // -------------------
        // PREFIX MATCH
        // -------------------
        if (name.startsWith(q)) {
            score += 500_000;
        }
    
        // -------------------
        // CONTAINS MATCH
        // -------------------
        else if (name.contains(q)) {
            score += 100_000;
        }
    
        // -------------------
        // TYPE BOOST (via instanceof)
        // -------------------
        if (item instanceof ArtistDTO) {
            score += 50_000;
        }
    
        if (item instanceof AlbumDTO album) {
            score += 40_000;
    
            // -------------------
            // ALBUM ARTIST BOOST
            // -------------------
            for (ArtistDTO artist : album.getArtists()) {
    
                String artistName = normalize(artist.getName());
    
                if (artistName.equals(q)) {
                    score += 300_000;
                }
                else if (artistName.contains(q)) {
                    score += 150_000;
                }
            }
        }
    
        return score;
    }

    private String normalize(String input) {
        if (input == null) return "";
    
        return input
                .toLowerCase()
                .trim()
                .replaceAll("\\s+", " "); // collapses multiple spaces
    }

    private int levenshtein(String a, String b) {

        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {

                if (i == 0)
                    dp[i][j] = j;
                else if (j == 0)
                    dp[i][j] = i;

                else if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(
                            dp[i - 1][j],
                            Math.min(dp[i][j - 1], dp[i - 1][j - 1]));
                }
            }
        }

        return dp[a.length()][b.length()];
    }

    // =========================
    // API CALL
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
                    String.class);

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
                extractImage(node));
    }

    private AlbumDTO mapAlbum(JsonNode node) {

        List<ArtistDTO> artists = new ArrayList<>();

        for (JsonNode artist : node.path("artists")) {
            artists.add(new ArtistDTO(
                    null,
                    artist.path("id").asText(),
                    artist.path("name").asText(),
                    "default"));
        }

        return new AlbumDTO(
                null,
                node.path("id").asText(),
                node.path("name").asText(),
                extractImage(node),
                artists);
    }

    // =========================
    // IMAGE
    // =========================

    private String extractImage(JsonNode node) {

        JsonNode images = node.path("images");

        if (images.isArray() && !images.isEmpty()) {
            return images.get(0).path("url").asText();
        }

        return "default";
    }
}