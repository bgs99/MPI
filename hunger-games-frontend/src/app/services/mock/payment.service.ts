import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';
import { ApiService } from '../api.service';

@Injectable({
    providedIn: 'root'
})
export class PaymentService {
    private static BASE_URL: string = `${ApiService.baseURL}/payment`;
    private static headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
    constructor(private http: HttpClient) { }
    async approve(orderId: number): Promise<void> {
        return await lastValueFrom(this.http.post<void>(
            PaymentService.BASE_URL,
            { orderId },
        ));
    }
}
