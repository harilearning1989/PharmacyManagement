import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-customer-search',
  templateUrl: './customer-search.component.html',
  styleUrls: ['./customer-search.component.scss']
})
export class CustomerSearchComponent implements OnInit {

  ngOnInit(): void {
  }

  customerForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.customerForm = this.fb.group({
      phone: [''],
      name: [''],
      dob: [''],
      gender: [''],
      email: ['']
    });
  }

  searchCustomer() {
    const phone = this.customerForm.get('phone')?.value;

    // TODO: Replace with real API
    if (phone === '9999999999') {
      this.customerForm.patchValue({
        name: 'Hari Reddy',
        dob: '1995-08-01',
        gender: 'Male',
        email: 'hari.reddy@example.com'
      });
    } else {
      this.customerForm.patchValue({
        name: '',
        dob: '',
        gender: '',
        email: ''
      });
    }
  }

}
