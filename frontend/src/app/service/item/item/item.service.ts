import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

import { Item } from '../../../core/model/item/item.type';
import { Artist } from '../../../core/model/item/artist.type';
import { Album } from '../../../core/model/item/album.type';

type SpecificItemInstance = Artist | Album; // song;
type ItemTypeName = 'ARTIST' | 'SONG' | 'ALBUM';

@Injectable({
  providedIn: 'root'
})
export class ItemService {

  protected apiUrl = 'http://localhost:8080/item';

  constructor(
    private http: HttpClient,
    private router: Router
  ) { }


  // CREATE
  addItem(request: {
    sourceId: string;
    type: string;
    provider: string;
  }): Observable<Item> {

    return this.http.post<Item>(
      `${this.apiUrl}/add`,
      request
    );

  }


  /**
   * Ensures an item exists in the database.
   * If it already exists, backend returns it.
   * Otherwise it is created and returned.
   */
  getOrCreateItem(
    item: Pick<Item, 'sourceId' | 'type' | 'provider'>
  ): Observable<Item> {

    return this.addItem({

      sourceId: item.sourceId,

      type: item.type,

      provider: item.provider

    });

  }


  /**
   * Used by lightweight components.
   * Resolves the item through the backend,
   * then navigates to the item page.
   */
  openItem(
    item: Pick<Item, 'sourceId' | 'type' | 'provider' | 'name' | 'imageURL'>
  ): void {
    console.log('Sending item:', item);
    this.getOrCreateItem({

      sourceId: item.sourceId,

      type: item.type,

      provider: item.provider

    })
      .subscribe({

        next: (savedItem) => {

          if (savedItem.id != null) {

            this.router.navigate([
              '/item',
              savedItem.id
            ]);

          }

        },

        error: (err) => {

          console.error(
            'Failed to open item:',
            err
          );

        }

      });

  }



  // READ
  getItemById(id: number): Observable<Item> {

    return this.http.get<Item>(
      `${this.apiUrl}/find/${id}`
    );

  }


  getItemBySourceId(sourceId: string): Observable<Item> {

    return this.http.get<Item>(
      `${this.apiUrl}/findSource/${sourceId}`
    );

  }


  getAllItems(): Observable<Item[]> {

    return this.http.get<Item[]>(
      this.apiUrl
    );

  }


  // DELETE
  deleteItem(id: number): Observable<void> {

    return this.http.delete<void>(
      `${this.apiUrl}/delete/${id}`
    );

  }

}