// src/app/shared/validators/username-validator.ts
import {AbstractControl, ValidationErrors} from '@angular/forms';

export function usernameValidator(control: AbstractControl): ValidationErrors | null {
  const value = control.value as string;
  if (!value) return null;

  // Rule 1: Only letters, digits, and dots, must start with a letter
  if (!/^[A-Za-z][A-Za-z0-9.]*$/.test(value)) {
    return {invalidChars: true};
  }

  // Rule 2: Cannot start with digit
  if (/^[0-9]/.test(value)) {
    return {startsWithDigit: true};
  }

  // Rule 3: No consecutive dots
  if (/\.\./.test(value)) {
    return {consecutiveDots: true};
  }

  // Rule 4: Maximum 2 dots allowed
  const dotCount = (value.match(/\./g) || []).length;
  if (dotCount > 2) {
    return {tooManyDots: true};
  }

  return null;
}
