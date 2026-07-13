import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Item } from '../../../core/model/item/item.type';
import { Album } from '../../../core/model/item/album.type';
import { Song } from '../../../core/model/item/song.type';

@Component({
  selector: 'app-item-preview',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './item-preview.component.html',
  styleUrl: '../item.component.css'
})
export class ItemPreviewComponent {

  @Input() item: Item | null = null;


  getArtists(): string {
    if (!this.item || this.item.type === 'artist') {
        return '';
    }

    return (this.item as Album | Song)
        .artists?.map(a => a.name)
        .join(', ') ?? '';
}

}