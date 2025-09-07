import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {CustomerService} from "../../services/customer.service";
import {Customer} from "../../models/customer";

@Component({
  selector: 'app-customer-search',
  templateUrl: './customer-search.component.html',
  styleUrls: ['./customer-search.component.scss']
})
export class CustomerSearchComponent implements OnInit {

  ngOnInit(): void {
  }

  customerForm: FormGroup;
  errorMessage: string = '';

  constructor(private fb: FormBuilder, private customerService: CustomerService) {
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
    this.errorMessage = '';

    if (!phone) {
      this.errorMessage = 'Please enter a phone number';
      return;
    }

    this.customerService.searchByPhone(phone).subscribe({
      next: (customer: Customer) => {
        this.customerForm.patchValue({
          name: customer.name,
          dob: customer.dob,
          gender: customer.gender,
          email: customer.email
        });
      },
      error: (err) => {
        this.customerForm.patchValue({
          name: '',
          dob: '',
          gender: '',
          email: ''
        });
        this.errorMessage = err.message;
      }
    });
  }

}
