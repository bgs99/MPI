import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiService } from '../api.service';

@Injectable({
    providedIn: 'root'
})
export class PaymentService {
    private static BASE_URL: string = `${ApiService.baseURL}/payment`;
    private static headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
    constructor(private http: HttpClient) { }
    approve(orderId: number): Observable<void> {
        return this.http.post<void>(
            PaymentService.BASE_URL,
            { orderId },
        );
    }
}
