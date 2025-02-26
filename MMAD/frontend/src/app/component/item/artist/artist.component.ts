import { Component, OnInit } from '@angular/core';
import { ArtistService } from '../../../service/item/artist/artist.service';
import { Artist } from '../../../model/item/artist.type';
import { catchError, of } from 'rxjs';
import { ItemComponent } from '../item.component';

@Component({
  selector: 'app-artist',
  templateUrl: './artist.component.html',
  styleUrls: ['./artist.component.css']
})
export class ArtistComponent extends ItemComponent implements OnInit {

  constructor(private artistService: ArtistService) {
    super(artistService);
  }

  ngOnInit(): void {
    
  }

  onSpotifyClick(): void {
    if (this.item?.getSourceId()) {  // Ensure item and sourceId are valid
      const spotifyUrl = `https://open.spotify.com/artist/${this.item.getSourceId()}`;
      window.open(spotifyUrl, "_blank");  // Opens Spotify page
    } else {
      console.error('Item or SourceId is missing');
    }
  }
  
}
