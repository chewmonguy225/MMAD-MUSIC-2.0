import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReviewBuilderComponent } from '../../review/review-builder/review-builder.component';
import { ItemComponent } from '../item.component';
import { Album } from '../../../model/item/album.type';
import { AlbumService } from '../../../service/item/album/album.service';


@Component({
  selector: 'app-album',
  templateUrl: './album.component.html',
  styleUrls: ['../item.component.css','./album.component.css'],
  imports: [FormsModule, CommonModule, ReviewBuilderComponent]
})
export class AlbumComponent extends ItemComponent {

  // Override input type from Item to Album
  @Input() override item: Album | null = null;


  constructor(protected override itemService: AlbumService) {
    super(itemService);
  }


  // Implement abstract method from base class
  onSpotifyClick(): void {
    if (!this.item) return;

    // For example, open the album Spotify page in a new tab
    // Assuming the Album sourceId is a Spotify ID, build the URL
    const spotifyUrl = `https://open.spotify.com/album/${this.item.getSourceId()}`;
    window.open(spotifyUrl, '_blank');
  }

  // You can add more album-specific methods or overrides here
}