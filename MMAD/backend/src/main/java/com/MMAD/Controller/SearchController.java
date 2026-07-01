package com.MMAD.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.MMAD.Service.SpotifyService;
import com.MMAD.Service.UserService;
import com.MMAD.dto.SearchResponse;
import com.MMAD.dto.item.ItemDTO;
import com.MMAD.dto.user.UserDTO;
import com.MMAD.model.item.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
                                ? List.of("artist", "album", "track", "user")
                                : Arrays.asList(type.toLowerCase().split(","));

                // -------------------------
                // SPOTIFY RESULTS
                // -------------------------
                List<ItemDTO> results = new ArrayList<>();

                List<String> spotifyTypes = types.stream()
                                .filter(t -> !t.equals("user"))
                                .toList();

                if (!spotifyTypes.isEmpty()) {
                        results.addAll(spotifyService.searchSpotify(query, spotifyTypes));
                }

                // -------------------------
                // USER RESULTS
                // -------------------------
                if (types.contains("user")) {
                        results.addAll(userService.searchUsers(query));
                }

                // -------------------------
                // RELEVANCE SORT (GLOBAL)
                // -------------------------
                results.sort(Comparator.comparingInt(ItemDTO::getRelevance));

                return ResponseEntity.ok(new SearchResponse(results));
        }
}