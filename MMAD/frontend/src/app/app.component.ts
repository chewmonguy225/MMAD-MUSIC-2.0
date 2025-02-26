import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ArtistComponent } from './component/item/artist/artist.component';
import { HeaderComponent } from './component/header/header.component';
import { SearchBarComponent } from './component/search-bar/search-bar.component';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, HeaderComponent, ArtistComponent, SearchBarComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
}
