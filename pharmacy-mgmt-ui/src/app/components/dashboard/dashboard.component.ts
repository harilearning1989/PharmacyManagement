import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

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

    // Simulated API response
    const mockCustomer = {
      name: 'Hari Reddy',
      dob: '1995-08-01',
      gender: 'Male',
      email: 'hari.reddy@example.com'
    };

    if (phone === '9999999999') {
      this.customerForm.patchValue(mockCustomer);
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
