import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {DashboardRoutingModule} from './dashboard-routing.module';
import {DashboardComponent} from "../../components/dashboard/dashboard.component";
import {ReactiveFormsModule} from "@angular/forms";
import {CustomerSearchModule} from "../customer-search/customer-search.module";
import {DoctorSearchModule} from "../doctor-search/doctor-search.module";
import {MedicineSearchModule} from "../medicine-search/medicine-search.module";
import {SupplierSearchModule} from "../supplier-search/supplier-search.module";
import {AppModule} from "../../app.module";


@NgModule({
  declarations: [
    DashboardComponent
  ],
  imports: [
    CommonModule,
    DashboardRoutingModule,
    ReactiveFormsModule,
    CustomerSearchModule,
    DoctorSearchModule,
    MedicineSearchModule,
    SupplierSearchModule,
    AppModule
  ]
})
export class DashboardModule {
}
