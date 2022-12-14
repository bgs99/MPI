import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';
import { Mentor } from '../models/mentor';
import { Order, OrderId, OrderResource, ResourceOrderRequest } from '../models/order';
import { ApiService } from './api.service';
import { Resource } from '../models/resource';
import { MentorTribute, TributeId } from '../models/tribute';

@Injectable({
    providedIn: 'root'
})
export class MentorsService {
    private static BASE_URL: string = `${ApiService.baseURL}/mentor`;
    constructor(private http: HttpClient) { }
    async getMentors(): Promise<Mentor[]> {
        return await lastValueFrom(this.http.get<Mentor[]>(
            `${MentorsService.BASE_URL}/all`
        ));
    }
    async getOrders(): Promise<Order[]> {
        return await lastValueFrom(this.http.get<Order[]>(
            `${MentorsService.BASE_URL}/orders`
        ));
    }
    async approve(orderId: OrderId, approved: boolean): Promise<void> {
        return await lastValueFrom(this.http.post<void>(
            `${MentorsService.BASE_URL}/order/approve`,
            { orderId, approved }
        ));
    }
    async requestResources(tributeId: TributeId, resources: Resource[]): Promise<Order> {
        return await lastValueFrom(this.http.post<Order>(
            `${MentorsService.BASE_URL}/order/create`,
            new ResourceOrderRequest(tributeId, resources)
        ))
    }
    async tributes(): Promise<MentorTribute[]> {
        return await lastValueFrom(this.http.get<MentorTribute[]>(
            `${MentorsService.BASE_URL}/tribute/all`
        ))
    }
}
