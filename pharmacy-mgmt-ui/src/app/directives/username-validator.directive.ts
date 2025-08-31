
import { Directive } from '@angular/core';
import { NG_VALIDATORS, Validator, AbstractControl, ValidationErrors } from '@angular/forms';
import {usernameValidator} from "../validators/username-validator";

@Directive({
  selector: '[appUsernameValidator]',
  providers: [
    {
      provide: NG_VALIDATORS,
      useExisting: UsernameValidatorDirective,
      multi: true
    }
  ]
})
export class UsernameValidatorDirective implements Validator {
  validate(control: AbstractControl): ValidationErrors | null {
    return usernameValidator(control);
  }
}
