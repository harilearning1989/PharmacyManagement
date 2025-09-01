import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Medicine} from "../models/medicine";

@Injectable({
  providedIn: 'root'
})
export class MedicineService {

  private baseUrl = '/api/medicines'; // adjust according to backend

  constructor(private http: HttpClient) {
  }

  searchMedicines(query: string): Observable<Medicine[]> {
    return this.http.get<Medicine[]>(`${this.baseUrl}/search?query=${query}`);
  }
}
