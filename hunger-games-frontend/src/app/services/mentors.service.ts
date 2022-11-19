import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';
import { AuthService } from './auth.service';
import { Mentor } from '../models/mentor';
import { Order } from '../models/order';
import { ApiService } from './api.service';

@Injectable({
    providedIn: 'root'
})
export class MentorsService {
    private static BASE_URL: string = `${ApiService.baseURL}/mentor`;
    private static headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
    constructor(private http: HttpClient, private auth: AuthService) { }
    async getMentors(): Promise<Mentor[]> {
        return await lastValueFrom(this.http.get<Mentor[]>(
            MentorsService.BASE_URL + "/all",
            { headers: MentorsService.headers },
        ));
    }
    async getOrders(): Promise<Order[]> {
        return await lastValueFrom(this.http.get<Order[]>(
            MentorsService.BASE_URL + "/orders",
            {
                headers: this.auth.authenticatedHeaders(MentorsService.headers),
                params: { mentorId: this.auth.id }
            },
        ));
    }
    async approve(mentorId: number, orderId: number, approved: boolean): Promise<void> {
        return await lastValueFrom(this.http.post<void>(
            MentorsService.BASE_URL + "/order/approve",
            { mentorId, orderId, approved },
            {
                headers: this.auth.authenticatedHeaders(MentorsService.headers),
            },
        ));
    }
}
