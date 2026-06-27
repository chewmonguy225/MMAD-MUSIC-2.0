package com.MMAD.MMAD.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.MMAD.MMAD.model.item.Album.Album;
import com.MMAD.MMAD.model.item.Artist.Artist;
import com.MMAD.MMAD.model.item.Song.Song;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SpotifyService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final String SEARCH_URL = "https://api.spotify.com/v1/search";

    private static final String CLIENT_ID = "39e68d74ea964fe8b12e4d03f28c817c";

    private static final String CLIENT_SECRET = "2e4bd1398d6e4e0aa43ed25e0116b4bf";
    private String accessToken;

    public SpotifyService() {
        this.accessToken = retrieveAccessToken();
    }

    // -------------------------
    // AUTH
    // -------------------------
    private String retrieveAccessToken() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String auth = CLIENT_ID + ":" + CLIENT_SECRET;
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
            return json.get("access_token").asText();

        } catch (Exception e) {
            throw new RuntimeException("Failed to get Spotify token", e);
        }
    }

    // -------------------------
    // PUBLIC API
    // -------------------------
    public List<Artist> searchArtists(String query) {
        JsonNode json = callSpotify(query, "artist");
        return parseArtists(json);
    }

    public List<Album> searchAlbums(String query) {
        JsonNode json = callSpotify(query, "album");
        return parseAlbums(json);
    }

    public List<Song> searchSongs(String query) {
        JsonNode json = callSpotify(query, "track");
        return parseSongs(json);
    }

    // -------------------------
    // HTTP CALL
    // -------------------------
    private JsonNode callSpotify(String query, String type) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = UriComponentsBuilder.fromUriString(SEARCH_URL)
                    .queryParam("q", query)
                    .queryParam("type", type)
                    .build()
                    .toUriString();

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            return objectMapper.readTree(response.getBody());

        } catch (Exception e) {
            throw new RuntimeException("Spotify API call failed", e);
        }
    }

    // -------------------------
    // PARSERS
    // -------------------------

    private List<Artist> parseArtists(JsonNode json) {
        List<Artist> list = new ArrayList<>();
        JsonNode nodes = json.path("artists").path("items");

        for (JsonNode n : nodes) {

            String id = n.path("id").asText();
            String name = n.path("name").asText();
            String image = extractImage(n);

            list.add(new Artist(id, name, image));
        }

        return list;
    }

    private List<Album> parseAlbums(JsonNode json) {
        List<Album> list = new ArrayList<>();
        JsonNode nodes = json.path("albums").path("items");

        for (JsonNode n : nodes) {

            String id = n.path("id").asText();
            String name = n.path("name").asText();
            String image = extractImage(n);

            list.add(new Album(image, id, name, new ArrayList<>()));
        }

        return list;
    }

    private List<Song> parseSongs(JsonNode json) {
        List<Song> list = new ArrayList<>();
        JsonNode nodes = json.path("tracks").path("items");

        for (JsonNode n : nodes) {

            String id = n.path("id").asText();
            String name = n.path("name").asText();

            JsonNode albumNode = n.path("album");
            String image = extractImage(albumNode);

            list.add(new Song(id, name, image, new ArrayList<>(), null));
        }

        return list;
    }

    // -------------------------
    // HELPER (NO ?: LOGIC)
    // -------------------------
    private String extractImage(JsonNode node) {

        String image = "default";

        JsonNode images = node.path("images");

        if (images.isArray()) {
            if (images.size() > 0) {
                JsonNode firstImage = images.get(0);

                if (firstImage.has("url")) {
                    image = firstImage.path("url").asText();
                }
            }
        }

        return image;
    }
}