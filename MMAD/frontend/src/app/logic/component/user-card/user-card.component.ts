import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-card',
  templateUrl: './user-card.component.html',
  styleUrls: ['./user-card.component.css'],
  standalone: true,
  // include needed imports if using standalone
})
export class UserCardComponent {
  @Input() user: any;

  constructor(private router: Router) {}

  goToProfile(username: string) {
    this.router.navigate(['/profile', username]);
  }
}
