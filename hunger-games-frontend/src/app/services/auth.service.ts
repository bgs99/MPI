import { Injectable } from '@angular/core';
import { HttpBackend, HttpClient } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';
import { ApiService } from './api.service';

class LoginResult {
    constructor(
        public id: string,
        public token: string,
        public type: string,
        public username: string,
        public name: string,
        public roles: string[]) { }
}

class AuthData {
    constructor(
        public id: string,
        public name: string,
        public token: string,
    ) { }
}

@Injectable()
export class AuthService {
    private BASE_URL: string = `${ApiService.baseURL}/auth`;
    private _authData: AuthData | null = null;
    private get authData(): AuthData {
        if (this._authData !== null) {
            return this._authData;
        }
        throw new Error("Accessing auth data before login");
    }

    get token(): string {
        return this.authData.token;
    }

    get name(): string {
        return this.authData.name;
    }

    get id(): string {
        return this.authData.id;
    }

    private http: HttpClient;
    constructor(backend: HttpBackend) {
        this.http = new HttpClient(backend);
    }

    async login(username: string, password: string): Promise<void> {
        const url: string = `${this.BASE_URL}/signin`;
        const login = await lastValueFrom(this.http.post<LoginResult>(url, { username, password }));
        this._authData = new AuthData(login.id, login.name, login.token);
    }

    async capitolAuth(username: string): Promise<void> {
        const url = `${ApiService.baseURL}/capitol/signin`;
        const login = await lastValueFrom(this.http.post<LoginResult>(url, { username }));
        this._authData = new AuthData(login.id, login.name, login.token);
    }

    async register(username: string, name: string, password: string): Promise<void> {
        const url: string = `${this.BASE_URL}/signup`;
        await lastValueFrom(this.http.post<void>(url, { username, name, password }));
    }
}
