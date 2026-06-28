package com.MMAD.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.MMAD.Service.SpotifyService;
import com.MMAD.Service.UserService;
import com.MMAD.dto.SearchResponse;
import com.MMAD.dto.item.ItemDTO;
import com.MMAD.model.User.UserDTO;
import com.MMAD.model.item.*;

import java.util.Arrays;
import java.util.List;;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SpotifyService spotifyService;
    private final UserService userService;

    public SearchController(SpotifyService spotifyService, UserService userService) {
        this.spotifyService = spotifyService;
        this.userService = userService;
    }

    @GetMapping("/{query}")
    public ResponseEntity<SearchResponse> search(
            @PathVariable String query,
            @RequestParam(required = false) String type) {

        List<String> types = (type == null || type.isBlank())
                ? List.of("artist", "album", "track")
                : Arrays.asList(type.toLowerCase().split(","));

        List<ItemDTO> spotifyResults = spotifyService.searchSpotify(query, types);

        /*
        List<UserDTO> users = types.contains("user")
                ? userService.searchUsers(query)
                : List.of();
        */

        return ResponseEntity.ok(
                new SearchResponse(spotifyResults)
        );
    }
}