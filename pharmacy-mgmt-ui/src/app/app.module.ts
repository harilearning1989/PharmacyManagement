import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {AuthLayoutComponent} from './components/auth-layout/auth-layout.component';
import {HttpClientModule} from "@angular/common/http";
import {ReactiveFormsModule} from "@angular/forms";
import { CustomerDialogComponent } from './components/dialog/customer-dialog/customer-dialog.component';
import { MedicineDialogComponent } from './components/dialog/medicine-dialog/medicine-dialog.component';
import { SupplierDialogComponent } from './components/dialog/supplier-dialog/supplier-dialog.component';
import { ReportDialogComponent } from './components/dialog/report-dialog/report-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    AuthLayoutComponent,
    CustomerDialogComponent,
    MedicineDialogComponent,
    SupplierDialogComponent,
    ReportDialogComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [],
  exports: [
    CustomerDialogComponent,
    SupplierDialogComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
