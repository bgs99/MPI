import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';
import { Sponsor } from '../models/sponsor';
import { ApiService } from './api.service';
import { Resource } from '../models/resource';
import { PaymentData } from '../models/payment-data';
import { ResourceOrderRequest } from '../models/order';


@Injectable({
    providedIn: 'root'
})
export class SponsorsService {
    private static BASE_URL: string = `${ApiService.baseURL}/sponsor`;
    constructor(private http: HttpClient) { }
    async getSponsors(): Promise<Sponsor[]> {
        return await lastValueFrom(this.http.get<Sponsor[]>(
            `${SponsorsService.BASE_URL}/all`
        ));
    }
    async orderResources(tributeId: number, resources: Resource[]): Promise<PaymentData> {
        return await lastValueFrom(this.http.post<PaymentData>(
            `${SponsorsService.BASE_URL}/send`,
            new ResourceOrderRequest(tributeId, resources)
        ));
    }
}
