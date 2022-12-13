import { Injectable } from '@angular/core';
import { HttpBackend, HttpClient } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';
import { ApiService } from '../api.service';
import { OrderId } from 'src/app/models/order';
import { PaymentResult } from 'src/app/models/payment';

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

export async function pay(orderId: OrderId): Promise<boolean> {
    const promise = new Promise<boolean>(resolve => {
        window.addEventListener('message', (event: MessageEvent) => {
            if (event.origin !== window.origin) { // Capitol origin
                return;
            }
            const data: PaymentResult = event.data;
            if (data.orderId != orderId) {
                return;
            }

            resolve(data.success);
        });
    });

    window.open(`/#/capitol/payment?id=${orderId}`);

    return promise;
}
