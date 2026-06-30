package com.MMAD.dto;

import java.util.List;
import com.MMAD.dto.item.ItemDTO;

public class SearchResponse {

    private List<ItemDTO> items;

    public SearchResponse() {}

    public SearchResponse(List<ItemDTO> items) {
        this.items = items;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }
}