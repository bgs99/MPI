import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';
import { AuthService } from './auth.service';
import { Tribute } from '../models/tribute';
import { PaymentData } from '../models/payment-data';
import { ApiService } from './api.service';

@Injectable({
    providedIn: 'root'
})
export class TributesService {
    private static BASE_URL: string = `${ApiService.baseURL}/tribute`;
    private static headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
    constructor(private http: HttpClient, private auth: AuthService) { }
    async getTributes(): Promise<Tribute[]> {
        return await lastValueFrom(this.http.get<Tribute[]>(
            TributesService.BASE_URL + "/all",
            { headers: TributesService.headers },
        ));
    }
    async orderAd(text: string): Promise<PaymentData> {
        return await lastValueFrom(this.http.post<PaymentData>(
            TributesService.BASE_URL + "/advertisement",
            { tributeId: this.auth.id, text },
            { headers: this.auth.authenticatedHeaders(TributesService.headers) },
        ));
    }
}
