import { Component, AfterViewInit, Renderer2 } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements AfterViewInit {
  title = 'frontend';

  constructor(private renderer: Renderer2) { }

  ngAfterViewInit(): void {
    const root = document.querySelector('app-root');

    if (root) {
      this.renderer.addClass(root, 'app-ready');
    }
  }
}