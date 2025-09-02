import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {MedicineService} from "../../services/medicine.service";
import {Medicine} from "../../models/medicine";

@Component({
  selector: 'app-medicine-search',
  templateUrl: './medicine-search.component.html',
  styleUrls: ['./medicine-search.component.scss']
})
export class MedicineSearchComponent implements OnInit {

  medicineForm: FormGroup;
  medicines: Medicine[] = [];
  searched = false;

  ngOnInit(): void {
    this.allMedicines();
  }

  constructor(private fb: FormBuilder, private medicineService: MedicineService) {
    this.medicineForm = this.fb.group({
      query: ['']
    });
  }

  searchMedicine() {
    const query = this.medicineForm.value.query;
    if (!query) return;

    this.medicineService.searchMedicines(query).subscribe({
      next: (res) => {
        this.medicines = res;
        this.searched = true;
      },
      error: () => {
        this.medicines = [];
        this.searched = true;
      }
    });
  }

  allMedicines() {
    this.medicineService.allMedicines().subscribe({
      next: (res) => {
        this.medicines = res;
        this.searched = true;
      },
      error: () => {
        this.medicines = [];
        this.searched = true;
      }
    });
  }

}
