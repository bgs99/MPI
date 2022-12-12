import { Injectable } from '@angular/core';
import { HttpBackend, HttpClient } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';
import { ApiService } from '../api.service';
import { OrderId } from 'src/app/models/order';

@Injectable({
    providedIn: 'root'
})
export class PaymentService {
    private static BASE_URL: string = `${ApiService.baseURL}/payment`;
    private http: HttpClient;
    constructor(backend: HttpBackend) {
        this.http = new HttpClient(backend);
    }
    async approve(orderId: OrderId): Promise<void> {
        return await lastValueFrom(this.http.post<void>(
            `${PaymentService.BASE_URL}/approve`,
            { orderId }
        ));
    }
}
