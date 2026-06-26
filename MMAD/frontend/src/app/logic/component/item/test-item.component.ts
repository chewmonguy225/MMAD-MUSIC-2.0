import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ItemComponent } from './item.component';
import { Item } from '../../model/item/item.type';

@Component({
  selector: 'app-test-item',
  standalone: true,
  template: `
    <img
      [src]="item?.getImageURL()"
      (click)="onImageClick()"
      alt="test"
    />
    <button *ngIf="showReviewButton">Write a Review</button>
  `,
  imports: [CommonModule],
})
export class TestItemComponent extends ItemComponent {
  override onSpotifyClick(): void {
    // Stub method for testing
  }
}
