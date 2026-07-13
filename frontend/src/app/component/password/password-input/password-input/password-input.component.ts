import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-password-input',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './password-input.component.html',
  styleUrls: ['./password-input.component.css']
})
export class PasswordInputComponent {

  @Input() label = 'Password';

  @Input() name = 'password';

  @Input() value = '';

  @Output() valueChange =
    new EventEmitter<string>();

  showPassword = false;


  updateValue(event: Event) {
    const input = event.target as HTMLInputElement;
    this.valueChange.emit(input.value);
  }

}