package com.MMAD.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.MMAD.Service.SearchService;
import com.MMAD.dto.SearchResponse;


@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;


    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }


    @GetMapping("/{query}")
    public ResponseEntity<SearchResponse> search(
            @PathVariable String query,
            @RequestParam(required = false) String type) {
                System.out.println("HERE");
        return ResponseEntity.ok(
                searchService.search(query, type)
        );
    }
}