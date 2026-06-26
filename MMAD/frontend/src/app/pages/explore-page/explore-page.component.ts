import { Component } from '@angular/core';
import { BasePageComponent } from '../base-page/base-page.component';
import { SearchBarComponent } from '../../logic/component/search-bar/search-bar.component';

@Component({
  selector: 'app-explore-page',
  imports: [BasePageComponent, SearchBarComponent],
  templateUrl: './explore-page.component.html',
  styleUrl: './explore-page.component.css'
})
export class ExplorePageComponent extends BasePageComponent {

}
