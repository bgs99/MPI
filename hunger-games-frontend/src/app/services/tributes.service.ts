import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';
import { Tribute, TributeId } from '../models/tribute';
import { PaymentData } from '../models/payment-data';
import { ApiService } from './api.service';

@Injectable({
    providedIn: 'root'
})
export class TributesService {
    private static BASE_URL: string = `${ApiService.baseURL}/tribute`;

    constructor(private http: HttpClient) { }
    async getTributes(): Promise<Tribute[]> {
        return await lastValueFrom(this.http.get<Tribute[]>(
            TributesService.BASE_URL + "/all"
        ));
    }

    async getTribute(id: TributeId): Promise<Tribute | undefined> {
        const tributes = await this.getTributes();
        return tributes.find(tribute => tribute.id === id);
    }

    async orderAd(text: string): Promise<PaymentData> {
        return await lastValueFrom(this.http.post<PaymentData>(
            TributesService.BASE_URL + "/advertisement",
            { text }
        ));
    }

    async getAds(): Promise<string[]> {
        return await lastValueFrom(this.http.get<string[]>(
            TributesService.BASE_URL + "/advertisement"
        ));
    }
}
