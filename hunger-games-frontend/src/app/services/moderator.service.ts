import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';
import { ApiService } from './api.service';
import { Ad } from '../models/ad';
import { OrderId } from '../models/order';

@Injectable()
export class ModeratorService {
    private BASE_URL: string = `${ApiService.baseURL}/moderator`;

    constructor(private http: HttpClient) { }

    async post(title: string, content: string): Promise<void> {
        const url: string = `${this.BASE_URL}/publish/news`;
        await lastValueFrom(this.http.post<void>(url, { name: title, content }));
    }

    async nextAd(): Promise<Ad> {
        const url: string = `${this.BASE_URL}/advertisingText`;
        return await lastValueFrom(this.http.get<Ad>(url));
    }

    async review(adId: OrderId, approved: boolean): Promise<void> {
        const url: string = `${this.BASE_URL}/advertisingText/approve`;
        await lastValueFrom(this.http.post<void>(url, {advertisingTextId: adId, approved}));
    }
}
