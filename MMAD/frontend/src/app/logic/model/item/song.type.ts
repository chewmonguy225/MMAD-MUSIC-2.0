// src/app/models/song.ts
import { Item } from "./item.type";   
import { Artist } from "./artist.type"; 
import { Album } from "./album.type";  

export class Song extends Item {
    private artists: Artist[]; 
    private album: Album;     

    constructor(
        id: number,
        sourceId: string,
        name: string,
        imageURL: string,

        artists: Artist[], // Song-specific constructor parameter
        album: Album       // Song-specific constructor parameter
    ) {
    
        super(id, sourceId, name, imageURL, 'SONG'); // Pass 'SONG' as the type
        this.artists = artists;
        this.album = album;
    }

    // Getter and Setter for artists
    getArtists(): Artist[] {
        return this.artists;
    }

    setArtists(artists: Artist[]): void {
        this.artists = artists;
    }

    // Getter and Setter for album
    getAlbum(): Album {
        return this.album;
    }

    setAlbum(album: Album): void {
        this.album = album;
    }

    // Static factory method to create a Song from JSON
    // REMOVED: 'override' keyword, as it's not applicable to static methods
    static fromJson(json: any): Song {
        return new Song(
            json.id,
            json.sourceId,
            json.name,
            json.imageURL,
            // REMOVED: json.createdAt, // These fields are not expected by the Song constructor now
            // REMOVED: json.updatedAt,
            // Map the nested artists array
            Array.isArray(json.artists)
                ? json.artists.map((artistJson: any) => Artist.fromJson(artistJson))
                : [],
            // Map the nested album object
            Album.fromJson(json.album)
        );
    }
}