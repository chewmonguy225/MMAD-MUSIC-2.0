package com.MMAD.dto.search;

public abstract class SearchResultDTO {

    private String name;
    private String imageURL;
    private String type;

    public SearchResultDTO(
            String name,
            String imageURL,
            String type) {
        this.name = name;
        this.imageURL = imageURL;
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getType() {
        return type;
    }
}