import { Component, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../../../service/user/auth/auth.service';
import { NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UiService } from '../../../../service/ui/ui.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, NgIf, FormsModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  title = signal('MMAD');
  currentUsername = signal<string | null>(null);
  searchQuery = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    public ui: UiService
  ) {
    this.loadUser();
  }

  private loadUser(): void {
    this.currentUsername.set(
      this.authService.getUsername()
    );
  }

  openModal() {
    this.ui.openReviewBuilder();
  }

  closeModal() {
    this.ui.closeReviewBuilder();
  }
}