import { Injectable } from '@angular/core';
import { HttpBackend, HttpClient } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';
import { ApiService } from './api.service';
import { PersonId, UserRole } from '../models/person';

class LoginResult {
    constructor(
        public id: PersonId,
        public token: string,
        public type: string,
        public username: string,
        public name: string,
        public roles: string[]) { }
}

class AuthData {
    constructor(
        public id: PersonId,
        public name: string,
        public token: string,
        public role: UserRole,
    ) { }
}

@Injectable()
export class AuthService {
    private get maybeAuthData(): AuthData | null {
        if (this._authData !== null) {
            return this._authData;
        }
        const storedAuth: string | null = localStorage.getItem('auth');
        if (storedAuth != null) {
            const authData = JSON.parse(storedAuth);
            this._authData = authData;
            return authData;
        }
        return null;
    }

    private BASE_URL: string = `${ApiService.baseURL}/auth`;
    private _authData: AuthData | null = null;
    private get authData(): AuthData {
        if (this.maybeAuthData !== null) {
            return this.maybeAuthData;
        }
        throw new Error("Accessing auth data before login");
    }

    get authenticated(): boolean {
        return this.maybeAuthData !== null;
    }

    get token(): string {
        return this.authData.token;
    }

    get name(): string {
        return this.authData.name;
    }

    get id(): PersonId {
        return this.authData.id;
    }

    get role(): UserRole {
        return this.authData.role;
    }

    private http: HttpClient;
    constructor(backend: HttpBackend) {
        this.http = new HttpClient(backend);
    }

    logout() {
        this._authData = null;
        localStorage.removeItem('auth');
    }

    private storeAuth(authData: AuthData): void {
        this._authData = authData;
        localStorage.setItem('auth', JSON.stringify(this._authData))
    }

    async loginModerator(username: string, password: string): Promise<void> {
        const url: string = `${ApiService.baseURL}/moderator/signin`;
        const login = await lastValueFrom(this.http.post<LoginResult>(url, { username, password }));
        this.storeAuth(new AuthData(login.id, login.name, login.token, UserRole.Moderator));
    }

    async login(username: string, password: string): Promise<void> {
        const url: string = `${this.BASE_URL}/signin`;
        const login = await lastValueFrom(this.http.post<LoginResult>(url, { username, password }));
        this.storeAuth(new AuthData(login.id, login.name, login.token, UserRole.Sponsor));
    }

    async capitolAuth(username: string, role: UserRole): Promise<void> {
        const url = `${ApiService.baseURL}/capitol/signin`;
        const login = await lastValueFrom(this.http.post<LoginResult>(url, { username }));
        this.storeAuth(new AuthData(login.id, login.name, login.token, role));
    }

    async register(username: string, name: string, password: string): Promise<void> {
        const url: string = `${this.BASE_URL}/signup`;
        await lastValueFrom(this.http.post<void>(url, { username, name, password }));
    }
}
