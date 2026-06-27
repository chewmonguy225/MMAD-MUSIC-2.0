package com.MMAD.MMAD.Service;

import java.util.*;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.MMAD.MMAD.dto.item.AlbumDTO;
import com.MMAD.MMAD.dto.item.ArtistDTO;
import com.MMAD.MMAD.dto.item.ItemDTO;
import com.MMAD.MMAD.dto.item.SongDTO;
import com.MMAD.MMAD.model.item.*;
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


    private String retrieveAccessToken() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String auth = CLIENT_ID + ":" + CLIENT_SECRET;
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
            return json.get("access_token").asText();

        } catch (Exception e) {
            throw new RuntimeException("Failed to get Spotify token", e);
        }
    }

    public List<ItemDTO> searchSpotify(String query, List<String> types) {

        JsonNode json = callSpotify(query, types);

        List<ItemDTO> results = new ArrayList<>();

        if (types.contains("artist")) {
            results.addAll(parseArtists(json));
        }

        return results;
    }

    private JsonNode callSpotify(String query, List<String> types) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            String typeParam = String.join(",", types);

            String url = UriComponentsBuilder.fromUriString(SEARCH_URL)
                    .queryParam("q", query)
                    .queryParam("type", typeParam)
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

    private List<ArtistDTO> parseArtists(JsonNode json) {
        List<ArtistDTO> list = new ArrayList<>();
        JsonNode nodes = json.path("artists").path("items");

        for (JsonNode n : nodes) {
            list.add(mapArtist(n));
        }

        return list;
    }

    private ArtistDTO mapArtist(JsonNode n) {
        return new ArtistDTO(
                null,
                n.path("id").asText(),
                n.path("name").asText(),
                extractImage(n)
            );
    }

    private String extractImage(JsonNode node) {

        JsonNode images = node.path("images");

        if (images.isArray() && images.size() > 0) {
            return images.get(0).path("url").asText();
        }

        return "default";
    }
}