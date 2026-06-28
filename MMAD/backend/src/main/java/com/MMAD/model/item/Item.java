package com.MMAD.model.item;

import jakarta.persistence.*;

import com.MMAD.dto.item.ItemDTO;
import com.MMAD.model.item.Item;

@Entity
@Table(name = "items")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "item_type")
public abstract class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String sourceId;

    @Column(nullable = false)
    private String name;

    @Column(name = "image_url")
    private String imageURL;
    

    // Constructors
    public Item() {
    }

    // Adjust constructor to remove itemType if you remove the field from Item
    public Item(String sourceId, String name, String imageURL) {
        this.sourceId = sourceId;
        this.name = name;
        this.imageURL = imageURL;
    }

    // Getters and Setters (adjust for itemType removal if you remove the field)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

}
