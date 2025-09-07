import { Component, OnInit } from '@angular/core';
import {Customer} from "../../../models/customer";
import {FormBuilder, Validators} from "@angular/forms";
import {CustomerService} from "../../../services/customer.service";

@Component({
  selector: 'app-supplier-dialog',
  templateUrl: './supplier-dialog.component.html',
  styleUrls: ['./supplier-dialog.component.scss']
})
export class SupplierDialogComponent implements OnInit {

  ngOnInit(): void {
  }

  activeTab: string = 'register';
  customers: Customer[] = [];
  loadingList = false;

  constructor(private fb: FormBuilder,
              private customerService: CustomerService) {}

  // Register Form
  registerForm = this.fb.group({
    name: ['', Validators.required],
    dob: [''],
    gender: [''],
    email: ['', [Validators.email]],
    phone: ['', Validators.required],
  });

  // Update Form
  updateForm = this.fb.group({
    customerId: ['', Validators.required],
    name: [''],
    email: [''],
    phone: [''],
  });

  submitRegister() {
    if (this.registerForm.valid) {
      console.log('Register new customer', this.registerForm.value);
    }
  }

  submitUpdate() {
    if (this.updateForm.valid) {
      const id = this.updateForm.value.customerId;
      /*this.customerService.updateCustomer(id, this.updateForm.value).subscribe(() => {
        this.loadCustomers();
        alert('Customer updated successfully!');
      });*/
    }
  }

}
