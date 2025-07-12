package com.MMAD.MMAD.model.Item;

import com.MMAD.MMAD.model.Item.Item;

public class ItemDTO {

    private Long id;
    private String sourceId;
    private String name;
    private String imageURL;

    // Constructors
    public ItemDTO() {}

    public ItemDTO(Long id, String sourceId, String name, String imageURL) {
        this.id = id;
        this.sourceId = sourceId;
        this.name = name;
        this.imageURL = imageURL;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSourceId() { return sourceId; }
    public void setSourceId(String sourceId) { this.sourceId = sourceId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImageURL() { return imageURL; }
    public void setImageURL(String imageURL) { this.imageURL = imageURL; }

    // Static mapper method from Item → ItemDTO
    public static ItemDTO fromEntity(Item item) {
        if (item == null) return null;

        return new ItemDTO(
            item.getId(),
            item.getSourceId(),
            item.getName(),
            item.getImageURL()
        );
    }
    
    public Item toEntity() {
        Item item = new Item();
        item.setId(this.id);            // Optional: set ID only if needed (e.g., for updates)
        item.setSourceId(this.sourceId);
        item.setName(this.name);
        item.setImageURL(this.imageURL);
        return item;
    }
}
