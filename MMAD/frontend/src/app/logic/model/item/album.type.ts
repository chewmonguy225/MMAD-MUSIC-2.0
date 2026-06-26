// src/app/models/album.ts
import { Item } from "./item.type"; // Ensure this imports your base Item class
import { Artist } from "./artist.type"; // Ensure this imports your Artist class

export class Album extends Item {
    private artists: Artist[]; // Album-specific property

    constructor(
        id: number,
        sourceId: string,
        name: string,
        imageURL: string,
        artists: Artist[] // Album-specific constructor parameter
    ) {
        super(id, sourceId, name, imageURL, 'ALBUM');
        this.artists = artists;
    }

    // Getter for artists
    getArtists(): Artist[] {
        return this.artists;
    }

    // Setter for artists
    setArtists(artists: Artist[]): void {
        this.artists = artists;
    }


    static fromJson(json: any): Album {
        return new Album(
            json.id,
            json.sourceId,
            json.name,
            json.imageURL,
            Array.isArray(json.artists)
                ? json.artists.map((artistJson: any) => Artist.fromJson(artistJson))
                : []
        );
    }
}