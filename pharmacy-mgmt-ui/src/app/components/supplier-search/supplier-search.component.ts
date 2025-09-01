import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-supplier-search',
  templateUrl: './supplier-search.component.html',
  styleUrls: ['./supplier-search.component.scss']
})
export class SupplierSearchComponent implements OnInit {

  ngOnInit(): void {
  }

  supplierForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.supplierForm = this.fb.group({
      phone: [''],
      name: [''],
      address: [''],
      email: [''],
      gstNumber: [''],
      drugLicenseNumber: [''],
    });
  }

  searchSupplier() {
    // Call API to fetch supplier by phone
    // Then patch values to form
    this.supplierForm.patchValue({
      name: 'John Doe',
      address: 'Hyderabad, Head Office',
      email: 'support@abcpharma.com',
      gstNumber: '36ABCDE1234F1Z5',
      drugLicenseNumber: 'DL-XYZ-56789',
    });
  }
}
