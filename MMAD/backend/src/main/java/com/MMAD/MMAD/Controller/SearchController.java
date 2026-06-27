package com.MMAD.MMAD.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import com.MMAD.MMAD.Service.SpotifyService;
import com.MMAD.MMAD.Service.UserService;
import com.MMAD.MMAD.dto.SearchResponse;
import com.MMAD.MMAD.dto.item.ItemDTO;
import com.MMAD.MMAD.model.User.UserDTO;
import com.MMAD.MMAD.model.item.*;;

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