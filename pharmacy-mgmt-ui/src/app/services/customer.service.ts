import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {Customer} from "../models/customer";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private baseUrl = 'http://localhost:8081/customer';

  constructor(private http: HttpClient) {
  }

  // 🔍 Search by phone
  searchByPhone(phone: string): Observable<Customer> {
    return this.http.get<Customer>(`${this.baseUrl}/search/by-phone`, {
      params: {phone}
    }).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';
    if (error.error instanceof ErrorEvent) {
      // Client-side
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.error}`;
    }
    return throwError(() => new Error(errorMessage));
  }

  getAllCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(`${this.baseUrl}/listAll`);
  }

  registerCustomer(data: Partial<Customer>): Observable<Customer> {
    return this.http.post<Customer>(`${this.baseUrl}`, data);
  }

}
