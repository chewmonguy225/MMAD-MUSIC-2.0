import { Component, Input} from '@angular/core';
import { ItemService } from '../../service/item/item.service';
import { Item } from '../../model/item/item.type';
import { catchError, of } from 'rxjs';

@Component({
  selector: 'app-item',
  imports: [],
  templateUrl: './item.component.html',
  styleUrl: './item.component.css'
})
export abstract class ItemComponent {
  @Input() item: Item | null = null;
  constructor(private itemService: ItemService) {}
  
  abstract onSpotifyClick(): void;
  
}
