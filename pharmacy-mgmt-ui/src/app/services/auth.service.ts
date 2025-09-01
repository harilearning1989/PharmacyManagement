import { Injectable } from '@angular/core';

// src/app/core/services/auth.service.ts
import { HttpClient } from '@angular/common/http';
import { map, Observable, tap } from 'rxjs';
import {environment} from "../../environments/environment";

export interface LoginResponse {
  token: string;
  expiresAt: string; // ISO date
  user: { id: string; email: string; name: string; role: string };
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = environment.apiUrl;
  private storageKey = 'pharmacy.auth';

  constructor(private http: HttpClient) {}

  login(username: string, password: string, remember: boolean): Observable<void> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/auth/login`, { username, password }).pipe(
      tap(res => this.saveSession(res, remember)),
      map(() => void 0),
    );
  }

  register(payload: { fullName: string; email: string; password: string }) {
    return this.http.post(`${this.baseUrl}/auth/register`, payload);
  }

  requestPasswordReset(email: string) {
    return this.http.post(`${this.baseUrl}/auth/forgot-password`, { email });
  }

  logout(): void {
    localStorage.removeItem(this.storageKey);
    sessionStorage.removeItem(this.storageKey);
  }

  get token(): string | null {
    return this.readSession()?.token ?? null;
  }

  isAuthenticated(): boolean {
    const s = this.readSession();
    return !!s && new Date(s.expiresAt) > new Date();
  }

  private saveSession(res: LoginResponse, remember: boolean) {
    const data = JSON.stringify(res);
    (remember ? localStorage : sessionStorage).setItem(this.storageKey, data);
    // clean the other storage to avoid conflicts
    (remember ? sessionStorage : localStorage).removeItem(this.storageKey);
  }

  private readSession(): LoginResponse | null {
    const raw = localStorage.getItem(this.storageKey) || sessionStorage.getItem(this.storageKey);
    return raw ? (JSON.parse(raw) as LoginResponse) : null;
  }
}

