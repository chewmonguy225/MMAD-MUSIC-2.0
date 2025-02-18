import { Component, OnInit, inject, signal } from '@angular/core';
import { ArtistService } from '../../service/artist/artist.service';
import { Artist } from '../../model/artist.type';
import { catchError, of } from 'rxjs';

@Component({
  selector: 'app-artist',
  templateUrl: './artist.component.html',
  styleUrls: ['./artist.component.css']
})
export class ArtistComponent implements OnInit {
  artist: Artist | null = null
  constructor(private artistService: ArtistService) {}

  ngOnInit(): void {
    this.artistService.getArtistWithId(11).subscribe({
      next: (data) => {
        this.artist = new Artist(data.id, data.sourceId, data.name, data.imageURL);
        console.log(this.artist);
      },
      error: (error) => {
        console.error('Error fetching artist:', error);
        this.artist = null;  // Handle error case
      }
    });
  }
  
}
