import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import { ItemService } from '../../service/item/item/item.service';
import { Item } from '../../core/model/item/item.type';
import { Artist } from '../../core/model/item/artist.type';
import { Album } from '../../core/model/item/album.type';
import { Song } from '../../core/model/item/song.type';

import { ItemCardSkeletonComponent } from './item-card-skeleton/item-card-skeleton.component';

@Component({
  selector:'app-item-card',
  standalone:true,
  imports:[
    CommonModule,
    ItemCardSkeletonComponent
  ],
  templateUrl:'./item.component.html',
  styleUrl:'./item.component.css'
})
export class ItemCardComponent {

  @Input() item!: Item;

  @Input() imageShape?: 'circle' | 'square';

  @Input() showArtists = false;

  @Input() navigateOnClick = true;

  isLoading = true;


  constructor(
    private itemService: ItemService,
    private router: Router
  ) { }


  ngOnInit(): void {

      this.isLoading = false;

  }


  get resolvedImageShape(): 'circle' | 'square' {

    if (this.imageShape) {
      return this.imageShape;
    }

    return this.item?.type === 'artist'
      ? 'circle'
      : 'square';

  }


  onItemClick(): void {

    if (!this.item || !this.navigateOnClick) {
      return;
    }

    this.itemService.getOrCreateItem(this.item)
      .subscribe({

        next:(savedItem)=>{

          if(savedItem.id != null){

            this.router.navigate([
              '/item',
              savedItem.id
            ]);

          }

        }

      });

  }


  getArtistNames(): string {

    if(!this.showArtists){
      return '';
    }

    if(this.item.type !== 'album' && this.item.type !== 'song'){
      return '';
    }

    const item = this.item as Album | Song;

    return item.artists
      ?.map((artist:Artist)=>artist.name)
      .join(', ') ?? '';

  }


  onSpotifyClick(event:MouseEvent):void{

    event.preventDefault();
    event.stopPropagation();

  }

}