import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';
import { ApiService } from './api.service';
import { Settings } from '../models/settings';


@Injectable({
    providedIn: 'root'
})
export class UserService {
    private static BASE_URL: string = `${ApiService.baseURL}/user`;
    constructor(private http: HttpClient) { }

    async setEmail(email: string): Promise<void> {
        await lastValueFrom(this.http.post<Settings>(
            `${UserService.BASE_URL}/settings`,
            { email }
        ));
    }

    async getEmail(): Promise<string> {
        const settings = await lastValueFrom(this.http.get<Settings>(
            `${UserService.BASE_URL}/settings`
        ));
        return settings.email;
    }
}
