import { Item } from './item.type';
import { Artist } from './artist.type';
import { Album } from './album.type';

export interface Song extends Item {
    artists: Artist[];
    album: Album;
    releaseDate: string;
    durationMs: number;
}