import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';
import { LoginResult } from '../models/auth';

@Injectable()
export class AuthService {
  private BASE_URL: string = 'http://localhost:42322/api/auth';
  private headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient) { }

  async login(username: string, password: string): Promise<LoginResult> {
    let url: string = `${this.BASE_URL}/signin`;
    return lastValueFrom(this.http.post<LoginResult>(url, { username, password }, { headers: this.headers }));
  }

  async register(username: string, password: string): Promise<void> {
    let url: string = `${this.BASE_URL}/signup`;
    return lastValueFrom(this.http.post<void>(url, { username, password }, { headers: this.headers }));
  }

  authenticatedHeaders(headers: HttpHeaders): HttpHeaders {
    let token: string | null = localStorage.getItem('token')
    if (token !== null) {
      return headers.append('Authorization', `Bearer ${token}`)
    }
    return headers;
  }
}
