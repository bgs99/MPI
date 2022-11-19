import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { lastValueFrom } from 'rxjs';
import { AuthService } from './auth.service';
import { Resource } from '../models/resource';
import { PaymentData } from '../models/payment-data';
import { ApiService } from './api.service';


class OrderDetail {
    resourceId: number;
    size: number;

    constructor(resource: Resource) {
        this.resourceId = resource.id;
        this.size = resource.amount;
    }
}

class SponsorResourceOrderRequest {
    orderDetails: OrderDetail[];

    constructor(public tributeId: number, public sponsorId: number, resources: Resource[]) {
        this.tributeId = tributeId;
        this.orderDetails = resources
            .filter((resource: Resource) => resource.amount > 0)
            .map((resource: Resource) => new OrderDetail(resource))
    }
}

@Injectable({
    providedIn: 'root'
})
export class ResourcesService {
    private static BASE_URL: string = `${ApiService.baseURL}/resource`;
    private static headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
    constructor(private http: HttpClient, private auth: AuthService) { }
    async getResources(): Promise<Resource[]> {
        return await lastValueFrom(this.http.get<Resource[]>(
            ResourcesService.BASE_URL + "/all",
            { headers: this.auth.authenticatedHeaders(ResourcesService.headers) },
        ));
    }
    async orderResources(tributeId: number, resources: Resource[]): Promise<PaymentData> {
        return await lastValueFrom(this.http.post<PaymentData>(
            ResourcesService.BASE_URL + "/send",
            new SponsorResourceOrderRequest(tributeId, this.auth.id, resources),
            { headers: this.auth.authenticatedHeaders(ResourcesService.headers) },
        ));
    }
}
