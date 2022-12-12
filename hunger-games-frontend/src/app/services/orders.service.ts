import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';
import { Order, OrderId } from '../models/order';
import { ApiService } from './api.service';

@Injectable({
    providedIn: 'root'
})
export class OrdersService {
    private static BASE_URL: string = `${ApiService.baseURL}/order`;
    constructor(private http: HttpClient) { }
    async getOrder(id: OrderId): Promise<Order> {
        return await lastValueFrom(this.http.get<Order>(
            `${OrdersService.BASE_URL}/${id}`
        ));
    }
}
