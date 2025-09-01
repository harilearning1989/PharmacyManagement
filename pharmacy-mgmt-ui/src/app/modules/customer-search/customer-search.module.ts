import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CustomerSearchComponent} from "../../components/customer-search/customer-search.component";
import {ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [
    CustomerSearchComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  exports: [CustomerSearchComponent]
})
export class CustomerSearchModule {
}
