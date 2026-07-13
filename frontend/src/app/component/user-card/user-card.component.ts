import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { UserDTO } from '../../service/user/user.service';

@Component({
  selector: 'app-user-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-card.component.html',
  styleUrls: ['./user-card.component.css']
})
export class UserCardComponent {

  @Input() user!: UserDTO;

  constructor(private router: Router) {}

  goToProfile(): void {
    this.router.navigate(['/profile', this.user.username]);
  }
}