import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
//import { LoginResult, User } from '../models/user';

@Injectable()
export class AuthService {
  private BASE_URL: string = 'http://localhost:5000/auth';
  private headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
  constructor(private http: HttpClient) { }
  /*login(user: User): Observable<LoginResult> {
    let url: string = `${this.BASE_URL}/login`;
    return this.http.post<LoginResult>(url, user, { headers: this.headers });
  }*/
  authenticatedHeaders(headers: HttpHeaders): HttpHeaders {
    let token: string | null = localStorage.getItem('token')
    if (token !== null) {
      return headers.append('Authorization', `Bearer ${token}`)
    }
    return headers;
  }
}
