import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';
import { Sponsor } from '../models/sponsor';
import { ApiService } from './api.service';
import { Resource } from '../models/resource';
import { PaymentData } from '../models/payment-data';
import { Order, ResourceOrderRequest } from '../models/order';
import { TributeId } from '../models/tribute';
import { News } from '../models/news';


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
    async orderResources(tributeId: TributeId, resources: Resource[]): Promise<PaymentData> {
        return await lastValueFrom(this.http.post<PaymentData>(
            `${SponsorsService.BASE_URL}/send`,
            new ResourceOrderRequest(tributeId, resources)
        ));
    }
    async resourcesToPayFor(): Promise<Map<string, Order[]>> {
        const orders = await lastValueFrom(this.http.get<Order[]>(
            `${SponsorsService.BASE_URL}/orders/approved`
        ));

        const result: Map<string, Order[]> = new Map();
        orders.forEach(order => {
            if (!result.has(order.tributeName)) {
                result.set(order.tributeName, []);
            }
            result.get(order.tributeName)?.push(order);
        });

        return result;
    }

    async isSubscribed(): Promise<boolean> {
        return await lastValueFrom(this.http.get<boolean>(
            `${SponsorsService.BASE_URL}/subscription`
        ));
    }

    async getNews(): Promise<News[]> {
        return await lastValueFrom(this.http.get<News[]>(
            `${SponsorsService.BASE_URL}/news`
        ));
    }

    private static SUBSCRIPTION_URL: string = `${SponsorsService.BASE_URL}/subscription`;

    async getSubscriptionPrice(): Promise<number> {
        return await lastValueFrom(this.http.get<number>(
            `${SponsorsService.SUBSCRIPTION_URL}/price`
        ));
    }

    async subscribe(email: string): Promise<Order> {
        return await lastValueFrom(this.http.post<Order>(
            `${SponsorsService.SUBSCRIPTION_URL}/news`,
            { email }
        ));
    }
}
