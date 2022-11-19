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

class AuthData {
    constructor(
        public id: number,
        public name: string,
        public token: string,
    ) { }
}

@Injectable()
export class AuthService {
    private BASE_URL: string = `${ApiService.baseURL}/auth`;
    private headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
    private _authData: AuthData | null = null;
    private get authData(): AuthData {
        if (this._authData !== null) {
            return this._authData;
        }
        throw new Error("Accessing auth data before login");
    }

    get name(): string {
        return this.authData.name;
    }

    get id(): number {
        return this.authData.id;
    }

    constructor(private http: HttpClient) { }

    async login(username: string, password: string): Promise<void> {
        const url: string = `${this.BASE_URL}/signin`;
        const login = await lastValueFrom(this.http.post<LoginResult>(url, { username, password }, { headers: this.headers }));
        this._authData = new AuthData(login.id, login.username, login.token);
    }

    async capitolAuth(username: string): Promise<void> {
        const url = `${ApiService.baseURL}/capitol/signin`;
        const login = await lastValueFrom(this.http.post<LoginResult>(url, { username }, { headers: this.headers }));
        this._authData = new AuthData(login.id, login.username, login.token);
    }

    async register(username: string, password: string): Promise<void> {
        const url: string = `${this.BASE_URL}/signup`;
        await lastValueFrom(this.http.post<void>(url, { username, password }, { headers: this.headers }));
    }

    authenticatedHeaders(headers: HttpHeaders): HttpHeaders {
        return headers.append('Authorization', `Bearer ${this.authData.token}`)
    }
}
