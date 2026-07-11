package com.MMAD.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.MMAD.dto.SearchResponse;
import com.MMAD.dto.item.ItemDTO;
import com.MMAD.dto.search.ItemSearchResultDTO;
import com.MMAD.dto.search.SearchResultDTO;
import com.MMAD.dto.search.UserSearchResultDTO;
import com.MMAD.dto.user.UserDTO;

@Service
public class SearchService {

    private final SpotifyService spotifyService;
    private final UserService userService;

    public SearchService(
            SpotifyService spotifyService,
            UserService userService) {

        this.spotifyService = spotifyService;
        this.userService = userService;
    }

    public SearchResponse search(String query, String type) {

        List<String> types = (type == null || type.isBlank())
                ? List.of("artist", "album", "track", "user")
                : Arrays.asList(type.toLowerCase().split(","));

        List<SearchResultDTO> results = new ArrayList<>();

        // Spotify
        List<String> spotifyTypes = types.stream()
                .filter(t -> !t.equals("user"))
                .toList();

        if (!spotifyTypes.isEmpty()) {

            results.addAll(
                    spotifyService.searchSpotify(query, spotifyTypes)
                            .stream()
                            .sorted(
                                    Comparator.comparingInt(ItemDTO::getRelevance)
                                            .reversed())
                            .map(ItemSearchResultDTO::fromDTO)
                            .toList());
        }

        // Users
        if (types.contains("user")) {

            results.addAll(
                    userService.searchUsers(query)
                            .stream()
                            .sorted(
                                    Comparator.comparingInt((UserDTO user) -> user.username()
                                            .equalsIgnoreCase(query)
                                                    ? 1_000_000
                                                    : 50_000)
                                            .reversed())
                            .map(UserSearchResultDTO::fromDTO)
                            .toList());
        }

        return new SearchResponse(results);
    }
}