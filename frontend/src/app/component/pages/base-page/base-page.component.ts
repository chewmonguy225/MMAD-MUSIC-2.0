import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './header/header.component';
import { ReviewBuilderComponent } from '../../review/review-builder/review-builder.component';

@Component({
  selector: 'app-base-page',
  standalone: true,
  imports: [
    RouterOutlet,
    HeaderComponent,
    ReviewBuilderComponent
  ],
  templateUrl: './base-page.component.html',
  styleUrl: './base-page.component.css'
})
export class BasePageComponent {}