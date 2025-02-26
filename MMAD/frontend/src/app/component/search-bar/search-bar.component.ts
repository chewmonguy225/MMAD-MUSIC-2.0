import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Artist } from '../../model/item/artist.type';
import { Item } from '../../model/item/item.type';
import { SpotifyService } from '../../service/externalAPI/spotify/spotify.service';
import { ItemComponent } from '../item/item.component';
import { ArtistComponent } from '../item/artist/artist.component';
import { ArtistService } from '../../service/item/artist/artist.service';


@Component({
  selector: 'app-search-bar',
  imports: [ArtistComponent, CommonModule],
  templateUrl: './search-bar.component.html',
  styleUrl: './search-bar.component.css'
})

export class SearchBarComponent {
  searchQuery: string = '';  // Variable to hold the search query
  results: Item[] | null = null;  // Variable to store the result
  item: Item | null = null;
  
  constructor(private externalAPIservice: SpotifyService, private artistService: ArtistService) {
  }

  onSearchInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.searchQuery = input.value;  // Store the value of the input
  }

  searchArtist(): void {
    console.log("here");
    if (this.searchQuery.trim() !== '') {
      this.externalAPIservice.searchArtist(this.searchQuery).subscribe({
        next: (artists: Artist[]) => {
          this.item = this.artistService.createArtist(artists[0])
          console.log(this.item);
          console.log(typeof this.item);
        },
        error: (err) => {
          console.error('Error fetching artists:', err);
          this.item = null;
        }
      });
    }
  }
}
