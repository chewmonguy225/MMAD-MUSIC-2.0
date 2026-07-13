import { Component, Input, OnChanges } from '@angular/core';
import { validatePassword } from '../../../../core/utils/form-validator';


@Component({
  selector: 'app-password-rules',
  standalone: true,
  templateUrl: './password-rules.component.html',
  styleUrls: ['./password-rules.component.css']
})

export class PasswordRulesComponent{

  @Input() password = '';

  get rules() {

    return validatePassword(this.password);

  }

}