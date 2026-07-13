import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SimplifiedSong } from '../../../../core/model/page/item-page.type';
import { ItemService } from '../../../../service/item/item/item.service';

@Component({
  selector: 'app-album-songs',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './album-songs.component.html',
  styleUrl: './album-songs.component.css'
})
export class AlbumSongsComponent {

  @Input() song!: SimplifiedSong;

  @Input() trackNumber = 0;


  constructor(
    private itemService: ItemService
  ) { }


  openSong(): void {

    this.itemService.openItem({

      sourceId: this.song.sourceId,
      name: this.song.name,
      imageURL: '',
      type: 'song',
      provider: this.song.provider

    });

  }


  formatDuration(durationMs: number | null | undefined): string {

    if (!durationMs) {
      return '';
    }

    const totalSeconds = Math.floor(durationMs / 1000);

    const minutes = Math.floor(totalSeconds / 60);

    const seconds = totalSeconds % 60;

    return `${minutes}:${seconds.toString().padStart(2, '0')}`;
  }

}