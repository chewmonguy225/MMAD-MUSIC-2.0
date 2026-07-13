import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-item-card-skeleton',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './item-card-skeleton.component.html',
  styleUrl: './item-card-skeleton.component.css'
})
export class ItemCardSkeletonComponent {

  @Input() imageShape: 'circle' | 'square' = 'square';

}