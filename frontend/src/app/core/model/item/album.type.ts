import { Item } from './item.type';
import { Artist } from './artist.type';

export interface Album extends Item {
    artists: Artist[];
    releaseDate: string;
}