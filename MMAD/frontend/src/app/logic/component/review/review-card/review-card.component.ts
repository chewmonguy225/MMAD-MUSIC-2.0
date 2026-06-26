import { Component, Input, OnInit } from '@angular/core'; // <-- Ensure OnInit is here
import { CommonModule } from '@angular/common';


import { Review } from '../../../model/review.type';
import { Album } from '../../../model/item/album.type';
import { Artist } from '../../../model/item/artist.type';
import { Song } from '../../../model/item/song.type';

@Component({
  selector: 'app-review-card',
  standalone: true,
  // Ensure all item components are imported here for ngSwitchCase
  imports: [CommonModule],
  templateUrl: './review-card.component.html',
  styleUrl: './review-card.component.css'
})

export class ReviewCardComponent { 
  @Input() review!: Review;

  get itemType(): string {
    return this.review?.item?.getType() || '';
  }

  get song(): Song | null {
    return this.review?.item?.getType() === 'SONG'
      ? this.review.item as Song
      : null;
  }
  
  get album(): Album | null {
    return this.review?.item?.getType() === 'ALBUM'
      ? this.review.item as Album
      : null;
  }
  
  get artist(): Artist | null {
    return this.review?.item?.getType() === 'ARTIST'
      ? this.review.item as Artist
      : null;
  }

  get stars(): number[] {
    return Array(this.review?.rating || 0);
  }
}