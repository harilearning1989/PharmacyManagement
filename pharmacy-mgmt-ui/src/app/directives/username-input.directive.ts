import {Directive, HostListener} from '@angular/core';
import {NgControl} from '@angular/forms';

@Directive({
  selector: '[appUsernameInput]' // 👈 use in template
})
export class UsernameInputDirective {
  constructor(private ngControl: NgControl) {
  }

  // Allowed: letters, numbers, dots
  private allowedPattern = /^[a-zA-Z0-9.]*$/;

  @HostListener('input', ['$event'])
  onInput(event: Event) {
    const input = event.target as HTMLInputElement;
    let value = input.value;

    // Remove invalid characters
    if (!this.allowedPattern.test(value)) {
      value = value.replace(/[^a-zA-Z0-9.]/g, '');
    }

    // Prevent starting with number
    if (/^[0-9]/.test(value)) {
      value = value.replace(/^[0-9]+/, '');
    }

    // Prevent consecutive dots
    value = value.replace(/\.{2,}/g, '.');

    // Limit to max 2 dots
    const dots = (value.match(/\./g) || []).length;
    if (dots > 2) {
      let count = 0;
      value = value.replace(/\./g, (m) => (++count > 2 ? '' : m));
    }

    // Update control value
    input.value = value;
    this.ngControl.control?.setValue(value, {emitEvent: false});
  }
}

