import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ArtistComponent } from './component/artist/artist.component';
import { HeaderComponent } from './header/header.component';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, HeaderComponent, ArtistComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
}
