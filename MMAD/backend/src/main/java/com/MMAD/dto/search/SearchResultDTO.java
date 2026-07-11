package com.MMAD.dto.search;

public abstract class SearchResultDTO {

    private Long id;
    private String name;
    private String imageURL;
    private String type;

    public SearchResultDTO(
            Long id,
            String name,
            String imageURL,
            String type) {
        this.id = id;
        this.name = name;
        this.imageURL = imageURL;
        this.type = type;
    }

    public Long getId() {
        return id;
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