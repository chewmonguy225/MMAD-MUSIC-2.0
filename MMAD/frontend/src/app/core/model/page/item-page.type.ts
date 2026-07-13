import { Album } from '../item/album.type';
import { Item, MusicProvider } from '../item/item.type';
import { ItemReviewViewModel } from '../review/ItemReviewsResponse';

export interface ItemPage {
  item: Item;

  reviews: ItemReviewViewModel[];

  songs?: SimplifiedSong[];
  albums?: Album[];

  albumDurationMs?: number;

  averageRating?: number;
  reviewCount?: number;
}

export interface SimplifiedSong {
  name: string;
  sourceId: string;
  provider: MusicProvider;
  durationMs: number;
  artists: string[];
}