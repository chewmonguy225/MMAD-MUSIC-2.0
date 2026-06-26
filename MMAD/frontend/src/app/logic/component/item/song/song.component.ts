import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { ReviewBuilderComponent } from '../../review/review-builder/review-builder.component';
import { ItemComponent } from '../item.component';
import { Song } from '../../../model/item/song.type';
import { SongService } from '../../../service/item/song/song.service';

@Component({
  selector: 'app-song',
  standalone: true,
  templateUrl: './song.component.html',
  styleUrls: ['../item.component.css', './song.component.css'],
  imports: [CommonModule, FormsModule, ReviewBuilderComponent]
})
export class SongComponent extends ItemComponent {
  @Input() override item: Song | null = null;

  constructor(protected override itemService: SongService) {
    super(itemService);
  }

  override onSpotifyClick(): void {
    if (!this.item) return;
    const spotifyUrl = `https://open.spotify.com/track/${this.item.getSourceId()}`;
    window.open(spotifyUrl, '_blank');
  }

}
