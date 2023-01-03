import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';
import { Tribute, TributeId } from '../models/tribute';
import { PaymentData } from '../models/payment-data';
import { ApiService } from './api.service';
import { Event, EventSerDe } from '../models/event';

@Injectable({
    providedIn: 'root'
})
export class TributesService {
    private static BASE_URL: string = `${ApiService.baseURL}/tribute`;

    constructor(private http: HttpClient) { }
    async getTributes(): Promise<Tribute[]> {
        return await lastValueFrom(this.http.get<Tribute[]>(
            TributesService.BASE_URL + "/all"
        ));
    }

    async getTribute(id: TributeId): Promise<Tribute | undefined> {
        const tributes = await this.getTributes();
        return tributes.find(tribute => tribute.id === id);
    }

    async orderAd(text: string): Promise<PaymentData> {
        return await lastValueFrom(this.http.post<PaymentData>(
            TributesService.BASE_URL + "/advertisement",
            { text }
        ));
    }

    async getAds(): Promise<string[]> {
        return await lastValueFrom(this.http.get<string[]>(
            TributesService.BASE_URL + "/advertisement"
        ));
    }

    async getEvents(): Promise<Event[]> {
        const events = await lastValueFrom(this.http.get<EventSerDe[]>(
            TributesService.BASE_URL + "/events"
        ));
        return events.map(event => Event.fromSerDe(event));
    }

    async addEvent(request: Event): Promise<void> {
        await lastValueFrom(this.http.post<void>(
            TributesService.BASE_URL + "/event",
            request.toSerDe(),
        ));
    }

    async editEvent(event: Event): Promise<void> {
        await lastValueFrom(this.http.post<void>(
            TributesService.BASE_URL + "/event/change",
            event.toSerDe(),
        ));
    }
}
