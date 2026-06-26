import { Component, Input } from '@angular/core';
import { ItemService } from '../../service/item/item/item.service';
import { Item } from '../../model/item/item.type';
import { CommonModule } from '@angular/common'; // Already imported
import { FormsModule } from '@angular/forms'; // Already imported
import { ReviewBuilderComponent } from '../review/review-builder/review-builder.component';

@Component({
  selector: 'app-item',
  imports: [FormsModule, CommonModule, ReviewBuilderComponent],
  templateUrl: './item.component.html',
  styleUrl: './item.component.css'
})
export abstract class ItemComponent { // It's an abstract class, which is fine
  @Input() item: Item | null = null;

  showReviewButton = false;
  showReviewInput = false;

  constructor(protected itemService: ItemService) {}

  abstract onSpotifyClick(): void;
}