import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { SearchBarComponent } from '../../search-bar/search-bar.component';
import { AuthService } from '../../../service/user/auth/auth.service';
import { UiService } from '../../../service/ui/ui.service';
import { UserService } from '../../../service/user/user.service';

@Component({
  selector: 'app-explore-page',
  standalone: true,
  imports: [SearchBarComponent],
  templateUrl: './explore-page.component.html',
  styleUrl: './explore-page.component.css'
})
export class ExplorePageComponent {

  constructor(
    private router: Router,
    private authService: AuthService,
    private userService: UserService,
    public ui: UiService
  ) {}

  onItemSelected(item: any) {
    this.router.navigate(['/item', item.id]);
  }
}