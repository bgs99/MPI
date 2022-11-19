import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';
import { ApiService } from './api.service';

class LoginResult {
  constructor(
    public id: number,
    public token: string,
    public type: string,
    public username: string,
    public roles: string[]) { }
}
@Injectable()
export class AuthService {
  private BASE_URL: string = `${ApiService.baseURL}/auth`;
  private headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
  private token: string = '';
  private _name: string = '';

  get name(): string { return this._name; }

  constructor(private http: HttpClient) { }

  async login(username: string, password: string): Promise<void> {
    let url: string = `${this.BASE_URL}/signin`;
    const login = await lastValueFrom(this.http.post<LoginResult>(url, { username, password }, { headers: this.headers }));
    this.token = login.token;
    this._name = login.username;
  }

  async register(username: string, password: string): Promise<void> {
    let url: string = `${this.BASE_URL}/signup`;
    await lastValueFrom(this.http.post<void>(url, { username, password }, { headers: this.headers }));
  }

  authenticatedHeaders(headers: HttpHeaders): HttpHeaders {
    return headers.append('Authorization', `Bearer ${this.token}`)
  }
}
