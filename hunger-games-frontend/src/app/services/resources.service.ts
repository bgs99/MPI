import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';
import { Resource } from '../models/resource';
import { ApiService } from './api.service';

@Injectable({
    providedIn: 'root'
})
export class ResourcesService {
    private static BASE_URL: string = `${ApiService.baseURL}/resource`;
    constructor(private http: HttpClient) { }
    async getResources(): Promise<Resource[]> {
        return await lastValueFrom(this.http.get<Resource[]>(
            ResourcesService.BASE_URL + "/all"
        ));
    }
}
