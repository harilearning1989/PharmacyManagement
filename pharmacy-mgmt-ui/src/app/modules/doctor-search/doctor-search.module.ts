import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DoctorSearchComponent} from "../../components/doctor-search/doctor-search.component";
import {ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [DoctorSearchComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  exports: [DoctorSearchComponent]
})
export class DoctorSearchModule {
}
