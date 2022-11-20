import { Injectable } from '@angular/core';
import { HttpBackend, HttpClient } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';
import { Mentor } from '../../models/mentor';
import { ApiService } from '../api.service';

@Injectable({
    providedIn: 'root'
})
export class MentorsService {
    private static BASE_URL: string = `${ApiService.baseURL}/capitol/mentor`;
    private http: HttpClient;
    constructor(backend: HttpBackend) {
        this.http = new HttpClient(backend);
    }
    async getAll(): Promise<Mentor[]> {
        return await lastValueFrom(this.http.get<Mentor[]>(
            `${MentorsService.BASE_URL}/all`
        ));
    }
}
