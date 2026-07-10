package com.MMAD.model.item;

import jakarta.persistence.*;

@Entity
@Table(name = "items", uniqueConstraints = {
        @UniqueConstraint(name = "uk_item_provider_source", columnNames = { "provider", "source_id" })
})
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "item_type")
public abstract class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sourceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MusicProvider provider;

    @Column(nullable = false)
    private String name;

    @Column(name = "image_url")
    private String imageURL;

    // Constructors

    public Item() {
    }

    public Item(
            String sourceId,
            MusicProvider provider,
            String name,
            String imageURL) {

        this.sourceId = sourceId;
        this.provider = provider;
        this.name = name;
        this.imageURL = imageURL;
    }

    // Getters and Setters

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

    public MusicProvider getProvider() {
        return provider;
    }

    public void setProvider(MusicProvider provider) {
        this.provider = provider;
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