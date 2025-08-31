import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {UsernameValidatorDirective} from "../../directives/username-validator.directive";
import {UsernameInputDirective} from "../../directives/username-input.directive";

@NgModule({
  declarations: [UsernameValidatorDirective,
    UsernameInputDirective],
  imports: [CommonModule],
  exports: [UsernameValidatorDirective,
    UsernameInputDirective] // 👈 export so it can be used in other modules
})
export class SharedModule {
}
