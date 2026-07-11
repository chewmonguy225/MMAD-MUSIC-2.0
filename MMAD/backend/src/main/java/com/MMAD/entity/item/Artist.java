// File: com/MMAD/MMAD/model/Item/Artist.java

package com.MMAD.entity.item;

import jakarta.persistence.DiscriminatorValue;
// REMOVE THIS: import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.PrimaryKeyJoinColumn; // NEW IMPORT

@Entity
@Table(name = "artists")
@DiscriminatorValue("artist")
@PrimaryKeyJoinColumn(name = "id")

public class Artist extends Item {

    public Artist() {
        super();
    }

    public Artist(
            String sourceId,
            MusicProvider provider,
            String name,
            String imageURL) {
        super(sourceId, provider, name, imageURL);
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + getId() +
                ", sourceId='" + getSourceId() + '\'' +
                ", name='" + getName() + '\'' +
                ", imageURL='" + getImageURL() + '\'' +
                // Add any unique Artist fields here:
                // ", genre='" + genre + '\'' +
                // ", countryOfOrigin='" + countryOfOrigin + '\'' +
                '}';
    }
}