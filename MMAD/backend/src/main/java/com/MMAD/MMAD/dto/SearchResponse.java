package com.MMAD.MMAD.dto;

import java.util.List;
import com.MMAD.MMAD.dto.item.*;

public class SearchResponse {

    private List<ItemDTO> items;

    public SearchResponse() {}

    public SearchResponse(List<ItemDTO> items) {
        this.items = items;
    }

    public List<ItemDTO> getArtists() {
        return items;
    }
}