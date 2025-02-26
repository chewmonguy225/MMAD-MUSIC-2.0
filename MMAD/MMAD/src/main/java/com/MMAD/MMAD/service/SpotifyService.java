package com.MMAD.MMAD.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

import com.MMAD.MMAD.model.Artist;
import com.MMAD.MMAD.model.Album;
import com.MMAD.MMAD.model.Song;
import com.MMAD.MMAD.model.Item;

@Service
public class SpotifyService {
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final String SEARCH_URL = "https://api.spotify.com/v1/search";
    private static final String CLIENT_ID = "39e68d74ea964fe8b12e4d03f28c817c";
    private static final String CLIENT_SECRET = "2e4bd1398d6e4e0aa43ed25e0116b4bf";

    private String accessToken;

    public SpotifyService() {
        this.accessToken = retrieveAccessToken(CLIENT_SECRET, CLIENT_ID);
    }

    private String retrieveAccessToken(String clientID, String clientSecret) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            String auth = CLIENT_ID + ":" + CLIENT_SECRET;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            headers.set("Authorization", "Basic " + encodedAuth);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "client_credentials");

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Failed to retrieve access token");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            return jsonResponse.get("access_token").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //type: artist or artist,track (for artist and track) or artist,track,album etc.
    public List<Item> searchItem(String itemName, String type) {
        List<Item> itemList = new ArrayList<>();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + this.accessToken);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = UriComponentsBuilder.fromUriString(SEARCH_URL)
                    .queryParam("q", itemName)
                    .queryParam("type", type)
                    .toUriString();

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());

            JsonNode artistsNode = jsonResponse.path("artists").path("items");
            if (artistsNode.isArray()) {
                for (JsonNode artistNode : artistsNode) {
                    JsonNode imagesNode = artistNode.path("images");
                    String imageUrl = imagesNode.isArray() && imagesNode.size() > 0 ? imagesNode.get(0).path("url").asText() : "default_image_url";
                    String id = artistNode.path("id").asText();
                    String name = artistNode.path("name").asText();

                    Artist artist = new Artist(imageUrl, id, name);
                    itemList.add(artist);
                }
            }

            // JsonNode albumNode = jsonResponse.path("album").path("items");
            // if (albumNode.isArray()) {
            //     for (JsonNode artistNode : albumNode) {
            //         JsonNode imagesNode = artistNode.path("images");
            //         String imageUrl = imagesNode.isArray() && imagesNode.size() > 0 ? imagesNode.get(0).path("url").asText() : "default_image_url";
            //         String id = artistNode.path("id").asText();
            //         String name = artistNode.path("name").asText();
            //         Album album = new Album(id, name, );

            //         public Album(String sourceID, String name, int artistID){
            //             super(sourceID, name);
            //             this.artistID = artistID;
            //         }
            //         itemList.add(artist);
            //     }
            // }

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return itemList;
    }
}
