import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReviewViewerComponent } from '../../../review/review-viewer/review-viewer.component';

@Component({
  selector: 'app-item-page-skeleton',
  standalone: true,
  imports: [
    CommonModule,
    ReviewViewerComponent
  ],
  templateUrl: './item-page-skeleton.component.html',
  styleUrl: './item-page-skeleton.component.css'
})
export class ItemPageSkeletonComponent {

}