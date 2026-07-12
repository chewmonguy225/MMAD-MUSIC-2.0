package com.MMAD.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
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
import com.MMAD.dto.item.SongDTO;
import com.MMAD.dto.page.ItemPageDTO;
import com.MMAD.entity.item.MusicProvider;
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

            JsonNode json = objectMapper.readTree(response.getBody());

            List<ItemDTO> results = new ArrayList<>();

            if (types.contains("artist")) {
                for (JsonNode node : json.path("artists").path("items")) {
                    results.add(mapArtist(node));
                }
            }

            if (types.contains("album")) {
                for (JsonNode node : json.path("albums").path("items")) {
                    results.add(mapAlbum(node));
                }
            }

            if (types.contains("track")) {
                for (JsonNode node : json.path("tracks").path("items")) {
                    results.add(mapSong(node));
                }
            }

            results.forEach(item -> item.setRelevance(scoreItem(query, item)));
            return results;

        } catch (Exception e) {
            throw new RuntimeException("Spotify search failed", e);
        }
    }

    // GET
    // ==========================

    // =========================
    // GET SINGLE ITEM
    // =========================

    public ArtistDTO getArtist(String artistId) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(getAccessToken());
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            String url = "https://api.spotify.com/v1/artists/" + artistId;

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class);

            JsonNode json = objectMapper.readTree(response.getBody());

            return mapArtist(json);

        } catch (Exception e) {
            throw new RuntimeException("Spotify artist lookup failed", e);
        }
    }

    public AlbumDTO getAlbum(String albumId) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(getAccessToken());
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            String url = "https://api.spotify.com/v1/albums/" + albumId;

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class);

            JsonNode json = objectMapper.readTree(response.getBody());

            return mapAlbum(json);

        } catch (Exception e) {
            throw new RuntimeException("Spotify album lookup failed", e);
        }
    }

    public SongDTO getSong(String songId) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(getAccessToken());
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            String url = "https://api.spotify.com/v1/tracks/" + songId;

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class);

            JsonNode json = objectMapper.readTree(response.getBody());

            return mapSong(json);

        } catch (Exception e) {
            throw new RuntimeException("Spotify song lookup failed", e);
        }
    }

    public List<ItemPageDTO.SimplifiedSong> getAlbumTracks(String albumId) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(getAccessToken());
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            String url = "https://api.spotify.com/v1/albums/" + albumId + "/tracks";

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class);

            JsonNode json = objectMapper.readTree(response.getBody());

            List<ItemPageDTO.SimplifiedSong> songs = new ArrayList<>();

            for (JsonNode track : json.path("items")) {
                songs.add(new ItemPageDTO.SimplifiedSong(
                        track.path("name").asText(),
                        track.path("id").asText(),
                        MusicProvider.SPOTIFY
                    )
                );
            }

            return songs;

        } catch (Exception e) {
            throw new RuntimeException("Spotify album tracks call failed", e);
        }
    }

    public List<AlbumDTO> getArtistAlbums(String artistId) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(getAccessToken());
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            String url = UriComponentsBuilder
                    .fromUriString("https://api.spotify.com/v1/artists/" + artistId + "/albums")
                    .queryParam("include_groups", "album")
                    .queryParam("limit", 50)
                    .build()
                    .toUriString();

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class);

            JsonNode json = objectMapper.readTree(response.getBody());

            List<AlbumDTO> albums = new ArrayList<>();

            for (JsonNode node : json.path("items")) {
                albums.add(mapAlbum(node));
            }

            return albums;

        } catch (Exception e) {
            throw new RuntimeException("Spotify artist albums call failed", e);
        }
    }

    // =========================
    // API CALL
    // =========================

    // =========================
    // MAPPERS
    // =========================
    private ArtistDTO mapArtist(JsonNode node) {
        return new ArtistDTO(
                null,
                node.path("id").asText(),
                MusicProvider.SPOTIFY,
                node.path("name").asText(),
                extractImage(node));
    }

    private AlbumDTO mapAlbum(JsonNode node) {

        List<ArtistDTO> artists = new ArrayList<>();

        for (JsonNode artist : node.path("artists")) {
            artists.add(new ArtistDTO(
                    null,
                    artist.path("id").asText(),
                    MusicProvider.SPOTIFY,
                    artist.path("name").asText(),
                    "default"));
        }

        return new AlbumDTO(
                null,
                node.path("id").asText(),
                MusicProvider.SPOTIFY,
                node.path("name").asText(),
                extractImage(node),
                artists);
    }

    private SongDTO mapSong(JsonNode node) {

        List<ArtistDTO> artists = new ArrayList<>();

        for (JsonNode artist : node.path("artists")) {
            artists.add(new ArtistDTO(
                    null,
                    artist.path("id").asText(),
                    MusicProvider.SPOTIFY,
                    artist.path("name").asText(),
                    "default"));
        }

        AlbumDTO album = mapAlbum(node.path("album"));

        return new SongDTO(
                null,
                node.path("id").asText(),
                MusicProvider.SPOTIFY,
                node.path("name").asText(),
                extractImage(node.path("album")),
                artists,
                album);
    }

    // Helpers
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
                } else if (artistName.contains(q)) {
                    score += 150_000;
                }
            }
        }

        return score;
    }

    private String normalize(String input) {
        if (input == null)
            return "";

        return input
                .toLowerCase()
                .trim()
                .replaceAll("\\s+", " "); // collapses multiple spaces
    }

    // =========================
    // IMAGE
    // =========================

    private String extractImage(JsonNode node) {

        JsonNode images = node.path("images");

        if (images.isArray() && !images.isEmpty()) {
            return images.get(0).path("url").asText();
        }

        return null;
    }
}