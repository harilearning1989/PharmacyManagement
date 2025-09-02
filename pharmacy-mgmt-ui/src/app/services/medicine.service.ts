import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Medicine} from "../models/medicine";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class MedicineService {

  //private baseUrl = '/api/medicines'; // adjust according to backend
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {
  }

  allMedicines(): Observable<Medicine[]> {
    return this.http.get<Medicine[]>(`${this.baseUrl}/medicines/listAll`);
  }

  searchMedicines(query: string): Observable<Medicine[]> {
    return this.http.get<Medicine[]>(`${this.baseUrl}/search?query=${query}`);
  }
}
