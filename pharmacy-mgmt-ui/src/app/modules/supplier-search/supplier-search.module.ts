import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SupplierSearchComponent} from "../../components/supplier-search/supplier-search.component";
import {ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [SupplierSearchComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  exports: [SupplierSearchComponent]
})
export class SupplierSearchModule {
}
