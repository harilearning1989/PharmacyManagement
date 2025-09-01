import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MedicineSearchComponent} from "../../components/medicine-search/medicine-search.component";
import {ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [MedicineSearchComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  exports: [MedicineSearchComponent]
})
export class MedicineSearchModule {
}
