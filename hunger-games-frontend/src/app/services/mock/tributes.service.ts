import { Injectable } from '@angular/core';
import { HttpBackend, HttpClient } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';
import { Tribute } from '../../models/tribute';
import { ApiService } from '../api.service';

@Injectable({
    providedIn: 'root'
})
export class TributesService {
    private static BASE_URL: string = `${ApiService.baseURL}/capitol/tribute`;
    private http: HttpClient;
    constructor(backend: HttpBackend) {
        this.http = new HttpClient(backend);
    }
    async getAll(): Promise<Tribute[]> {
        return await lastValueFrom(this.http.get<Tribute[]>(
            `${TributesService.BASE_URL}/all`,
        ));
    }
}
