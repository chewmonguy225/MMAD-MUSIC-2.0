import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Artist } from '../../model/item/artist.type';
import { Album } from '../../model/item/album.type';
import { Song } from '../../model/item/song.type';
import { Item } from '../../model/item/item.type';
import { UserDTO } from '../../service/user/user.service';

import { SpotifyService } from '../../service/externalAPI/spotify/spotify.service';
import { ArtistService } from '../../service/item/artist/artist.service';
import { AlbumService } from '../../service/item/album/album.service';
import { SongService } from '../../service/item/song/song.service';
import { UserService } from '../../service/user/user.service';

import { ArtistComponent } from '../item/artist/artist.component';
import { AlbumComponent } from '../item/album/album.component';
import { SongComponent } from '../item/song/song.component';
import { UserCardComponent } from '../user-card/user-card.component';

@Component({
  selector: 'app-search-bar',
  standalone: true,
  imports: [ArtistComponent, AlbumComponent, SongComponent, UserCardComponent, CommonModule],
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css']
})
export class SearchBarComponent {
  searchQuery: string = '';

  //for items
  results: (Item)[] = [];
  displayedItems: (Item)[] = [];
  //for users
  userResults: UserDTO[] = [];
  displayedUserResults: UserDTO[] = [];

  currentPage: number = 1;
  itemsPerPage: number = 5;
  searchType: 'artist' | 'album' | 'song' | 'user' = 'artist';
  

  constructor(
    private externalAPIservice: SpotifyService,
    private artistService: ArtistService,
    private albumService: AlbumService,
    private songService: SongService,
    private userService: UserService
  ) { }

  onSearchInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.searchQuery = input.value;
  }

  searchAll(): void {
    if (this.searchQuery.trim() === '') {
      this.results = [];
      this.userResults = [];
      this.displayedItems = [];
      this.displayedUserResults = [];
      this.currentPage = 1;
      return;
    }

  
    const artist$ = this.artistService.searchArtists(this.searchQuery);
    const album$ = this.albumService.searchAlbums(this.searchQuery);
    const song$ = this.songService.searchSongs(this.searchQuery);
    const user$ = this.userService.searchUsers(this.searchQuery);
  
    // Use forkJoin to wait for all observables to complete
    import('rxjs').then(rxjs => {
      rxjs.forkJoin([artist$, album$, song$, user$]).subscribe({
        next: ([artists, albums, songs, users]) => {
          this.results = [
            ...artists,
            ...albums.map(album => Album.fromJson(album)),
            ...songs.map(song => Song.fromJson(song))
          ];
          this.userResults = users;
  
          this.currentPage = 1;
          this.updateDisplayedItems();
        },
        error: () => {
          this.results = [];
          this.userResults = [];
        }
      });
    });
  }
  

  searchArtist(): void {
    this.searchType = 'artist';
    if (this.searchQuery.trim() !== '') {
      this.artistService.searchArtists(this.searchQuery).subscribe({
        next: (artists: Artist[]) => {
          this.results = artists;
          this.currentPage = 1;
          this.updateDisplayedItems();
        },
        error: () => {
          this.results = [];
        }
      });
    } else {
      this.results = [];
      this.currentPage = 1;
    }
  }

  searchAlbum(): void {
    this.searchType = 'album';
    if (this.searchQuery.trim() !== '') {
      this.albumService.searchAlbums(this.searchQuery).subscribe({
        next: (albums: Album[]) => {
          // Convert plain objects to instances of Album class
          this.results = albums.map(album => Album.fromJson(album));
          this.currentPage = 1;
          this.updateDisplayedItems();
        },
        error: () => {
          this.results = [];
        }
      });
    } else {
      this.results = [];
      this.currentPage = 1;
    }
  }

  searchSong(): void {
    this.searchType = 'song';
    if (this.searchQuery.trim() !== '') {
      this.songService.searchSongs(this.searchQuery).subscribe({
        next: (songs: Song[]) => {
          // Convert plain objects to instances of Song class
          this.results = songs.map(song => Song.fromJson(song));
          this.currentPage = 1;
          this.updateDisplayedItems();
        },
        error: () => {
          this.results = [];
        }
      });
    } else {
      this.results = [];
      this.currentPage = 1;
    }
  }

  searchUser(): void {
    this.searchType = 'user';
    if (this.searchQuery.trim() !== '') {
      this.userService.searchUsers(this.searchQuery).subscribe({
        next: (users: UserDTO[]) => {
          this.userResults = users;
          this.currentPage = 1;
          this.updateDisplayedItems();
        },
        error: () => {
          this.userResults = [];
        }
      });
    } else {
      this.userResults = [];
      this.currentPage = 1;
    }
  }
  


  updateDisplayedItems(): void {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
  
    if (this.searchType === 'user') {
      this.displayedUserResults = this.userResults.slice(startIndex, endIndex);
    } else {
      this.displayedItems = this.results.slice(startIndex, endIndex);
    }
  }
  

  nextPage(): void {
    if (this.currentPage * this.itemsPerPage < this.results.length) {
      this.currentPage++;
      this.updateDisplayedItems();
    }
  }

  prevPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.updateDisplayedItems();
    }
  }

  getTotalPages(): number {
    return Math.ceil(this.results.length / this.itemsPerPage);
  }

  // Type guards for template

  isArtist(item: Item): item is Artist {
    return this.searchType === 'artist';
  }

  isAlbum(item: Item): item is Album {
    return this.searchType === 'album';
  }

  isSong(item: Item): item is Song {
    return this.searchType === 'song';
  }
}
