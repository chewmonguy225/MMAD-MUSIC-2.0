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

import com.MMAD.MMAD.model.Item.Item;
import com.MMAD.MMAD.model.Item.Album.Album;
//import com.MMAD.MMAD.model.Item.Song;
import com.MMAD.MMAD.model.Item.Artist.Artist;
import com.MMAD.MMAD.model.Item.Song.Song;

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

            ResponseEntity<String> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, requestEntity,
                    String.class);

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

    // type: artist or artist,track (for artist and track) or artist,track,album
    // etc.
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

            // ARTIST
            JsonNode artistsNode = jsonResponse.path("artists").path("items");
            if (artistsNode.isArray()) {
                for (JsonNode artistNode : artistsNode) {
                    JsonNode imagesNode = artistNode.path("images");
                    String imageUrl = imagesNode.isArray() && imagesNode.size() > 0
                            ? imagesNode.get(0).path("url").asText()
                            : "default_image_url";
                    String spotifyId = artistNode.path("id").asText(); // Spotify ID, maps to your sourceId
                    String name = artistNode.path("name").asText();

                    Artist artist = new Artist(spotifyId, name, imageUrl);
                    itemList.add(artist);
                }
            }

            // ALBUMS
            JsonNode albumsNode = jsonResponse.path("albums").path("items");
            if (albumsNode.isArray()) {
                for (JsonNode albumNode : albumsNode) {
                    JsonNode imagesNode = albumNode.path("images");
                    String imageUrl = imagesNode.isArray() && imagesNode.size() > 0
                            ? imagesNode.get(0).path("url").asText()
                            : "default_image_url";
                    String spotifyId = albumNode.path("id").asText(); // Spotify ID, maps to your sourceId
                    String name = albumNode.path("name").asText();

                    List<Artist> albumArtistsList = new ArrayList<>();
                    JsonNode albumArtistsNode = albumNode.path("artists");
                    for (JsonNode albumArtistNode : albumArtistsNode) {
                        String albumArtistSpotifyId = albumArtistNode.path("id").asText();
                        String albumArtistName = albumArtistNode.path("name").asText();

                        Artist albumArtist = new Artist(albumArtistSpotifyId, albumArtistName, "default_image_url");
                        albumArtistsList.add(albumArtist);
                    }   

                    Album album = new Album(imageUrl,spotifyId , name, albumArtistsList);
                    itemList.add(album);
                }
            }

            //SONGS

            JsonNode songsNode = jsonResponse.path("tracks").path("items");
            if (songsNode.isArray()) {
                for (JsonNode songNode : songsNode) {
                    String sourceId = songNode.path("id").asText();
                    String name = songNode.path("name").asText();
                    String imageUrl;
                //Album that song is a part of
                    JsonNode songAlbumNode = songNode.path("album");
                    String songAlbumName = songAlbumNode.path("name").asText();
                    //Use album cover as song cover
                    JsonNode imagesNode = songAlbumNode.path("images");
                    imageUrl = imagesNode.isArray() && imagesNode.size() > 0
                            ? imagesNode.get(0).path("url").asText()
                            : "default_image_url";
                    String songAlbumSpotifyId = songAlbumNode.path("id").asText();

                    List<Artist> songAlbumArtistsList = new ArrayList<>();
                    JsonNode songAlbumArtistsNode = songAlbumNode.path("artists");
                    for (JsonNode songAlbumArtistNode : songAlbumArtistsNode) {
                        String albumArtistSpotifyId = songAlbumArtistNode.path("id").asText();
                        String albumArtistName = songAlbumArtistNode.path("name").asText();

                        Artist albumArtist = new Artist(albumArtistSpotifyId, albumArtistName, "default_image_url");
                        songAlbumArtistsList.add(albumArtist);
                    }   
                    Album album = new Album(imageUrl, songAlbumSpotifyId, songAlbumName, songAlbumArtistsList);

                //Song's Artist because Song can have a featured artist that is not considered in the album object
                    List<Artist> songArtistsList = new ArrayList<>();
                    JsonNode songArtistsNode = songNode.path("artists");
                    for (JsonNode songArtistNode : songArtistsNode) {
                        String songArtistSpotifyId = songArtistNode.path("id").asText();
                        String songArtistName = songArtistNode.path("name").asText();

                        Artist songArtist = new Artist(songArtistSpotifyId, songArtistName, "default_image_url");
                        songArtistsList.add(songArtist);
                    }   
                    
                    Song song = new Song(sourceId, name, imageUrl, songArtistsList, album);
                    itemList.add(song);
                }
            }

        } catch (Exception e) {
            System.err.println("Error searching Spotify items: " + e.getMessage());
            e.printStackTrace();
        }
        return itemList;
    }

}
