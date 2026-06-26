package com.MMAD.MMAD.model.Item;

public class SearchResultDTO {

    private String type; // "artist" | "album" | "song"
    private Object data;

    public SearchResultDTO(String type, Object data) {
        this.type = type;
        this.data = data;
    }

    public String getType() { return type; }
    public Object getData() { return data; }
}