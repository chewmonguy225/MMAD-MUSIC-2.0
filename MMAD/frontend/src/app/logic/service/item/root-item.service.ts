import { Injectable } from '@angular/core';
import { Observable, throwError, forkJoin, map } from 'rxjs';
import { Item } from '../../model/item/item.type';
import { Artist } from '../../model/item/artist.type';
import { Album } from '../../model/item/album.type'; // Import Album type

import { ArtistService } from './artist/artist.service';
import { AlbumService } from './album/album.service'; // Import AlbumService
import { ItemService } from './item/item.service';
import { SongService } from './song/song.service';
import { Song } from '../../model/item/song.type';

type SpecificItemInstance = Artist | Album | Song; // Album added, Song still excluded
type ItemTypeName = 'ARTIST' | 'SONG' | 'ALBUM'; 

@Injectable({
  providedIn: 'root'
})
export class RootItemService {

  constructor(
    private itemService: ItemService,
    private artistService: ArtistService,
    private songService: SongService,
    private albumService: AlbumService
  ) { }

  // CREATE
  addItem(item: SpecificItemInstance): Observable<SpecificItemInstance> {
    console.log('ItemService (Create): Dispatching item for instance:', item.constructor.name);

    if (item instanceof Artist) {
      return this.artistService.addArtist(item);
    } else if (item instanceof Song) {
      return this.songService.addSong(item);
    } else if (item instanceof Album) {
      return this.albumService.addAlbum(item);
    } else {
      const unknownItem = item as any;
      return throwError(() => new Error(`Unsupported item instance type for addition: ${unknownItem.constructor.name}`));
    }
  }

  // READ
  getItemById(id: number, type?: ItemTypeName): Observable<SpecificItemInstance | Item> {
    console.log(`ItemService (Read): Getting item with ID ${id} of type: ${type || 'GENERIC'}`);

    if (type) {
      switch (type) {
        case 'ARTIST':
          return this.artistService.getArtistById(id);
        case 'SONG':
          return this.songService.getSongById(id);
        case 'ALBUM':
          return this.albumService.getAlbumById(id);
        default:
          return throwError(() => new Error(`Unsupported specific item type for retrieval: ${type}`));
      }
    } else {
      return this.itemService.getItemById(id);
    }
  }

  getAllItems(): Observable<SpecificItemInstance[]> {
    console.log('ItemService (Read): Getting ALL items (Artists, Albums) combined.');
    return forkJoin([
      this.artistService.getAllArtists(),
      this.songService.getAllSongs(), 
      this.albumService.getAllAlbums()
    ]).pipe(
      map(([artists, songs, albums]) => [...artists, ...songs, ...albums])
    );
  }

  getAllItemsByType(type: ItemTypeName): Observable<SpecificItemInstance[]> {
    console.log(`ItemService (Read): Getting all items of type: ${type}`);
    switch (type) {
      case 'ARTIST':
        return this.artistService.getAllArtists() as Observable<Artist[]>;
      case 'SONG':
      return this.songService.getAllSongs() as Observable<Song[]>;
      case 'ALBUM':
        return this.albumService.getAllAlbums() as Observable<Album[]>;
      default:
        return throwError(() => new Error(`Unsupported item type for all retrieval: ${type}`));
    }
  }

  // UPDATE
  updateItem(item: SpecificItemInstance): Observable<SpecificItemInstance> {
    console.log('ItemService (Update): Dispatching item update for instance:', item.constructor.name);

    if (item instanceof Artist) {
      return this.artistService.updateArtist(item);
    } else if (item instanceof Song) {
      return this.songService.updateSong(item);
    } else if (item instanceof Album) {
      return this.albumService.updateAlbum(item);
    } else {
      const unknownItem = item as any;
      return throwError(() => new Error(`Unsupported item instance type for update: ${unknownItem.constructor.name}`));
    }
  }

  // DELETE
  deleteItem(id: number, type?: ItemTypeName): Observable<void> {
    console.log(`ItemService (Delete): Dispatching item deletion for ID ${id} of type: ${type || 'GENERIC'}`);

    if (type) {
      switch (type) {
        case 'ARTIST':
          return this.artistService.deleteArtist(id);
        case 'SONG':
          return this.songService.deleteSong(id);
        case 'ALBUM':
          return this.albumService.deleteAlbum(id);
        default:
          return throwError(() => new Error(`Unsupported specific item type for deletion: ${type}`));
      }
    } else {
      return this.itemService.deleteItem(id);
    }
  }

  // SEARCH
  searchItems(query: string, type?: ItemTypeName): Observable<SpecificItemInstance[]> {
    console.log(`ItemService (Search): Searching for "${query}" of type: ${type || 'ALL'}`);

    if (type) {
      switch (type) {
        case 'ARTIST':
          return this.artistService.searchArtists(query);
        case 'SONG':
          return this.songService.searchSongs(query);
        case 'ALBUM':
          return this.albumService.searchAlbums(query);
        default:
          return throwError(() => new Error(`Unsupported item type for search: ${type}`));
      }
    } else {
      return forkJoin([
        this.artistService.searchArtists(query),
        this.songService.searchSongs(query),
        this.albumService.searchAlbums(query)
      ]).pipe(
        map(([artists, songs, albums]) => [...artists, ...songs, ...albums])
      );
    }
  }
}
