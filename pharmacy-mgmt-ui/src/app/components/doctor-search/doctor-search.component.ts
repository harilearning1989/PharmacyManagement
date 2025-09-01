import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-doctor-search',
  templateUrl: './doctor-search.component.html',
  styleUrls: ['./doctor-search.component.scss']
})
export class DoctorSearchComponent implements OnInit {

  ngOnInit(): void {
  }

  doctorForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.doctorForm = this.fb.group({
      doctorName: [''],
      name: [''],
      specialist: [''],
      experience: [''],
      email: [''],
      qualification: ['']
    });
  }

  searchDoctor() {
    const enteredName = this.doctorForm.value.doctorName;

    // 🔹 Replace with real API call
    if (enteredName.toLowerCase() === 'hari') {
      this.doctorForm.patchValue({
        name: 'Dr. Hari Reddy',
        specialist: 'Cardiologist',
        experience: '12 years',
        email: 'hari.reddy@example.com',
        qualification: 'MD, DM (Cardiology)'
      });
    } else {
      this.doctorForm.patchValue({
        name: '',
        specialist: '',
        experience: '',
        email: '',
        qualification: ''
      });
    }
  }

}
