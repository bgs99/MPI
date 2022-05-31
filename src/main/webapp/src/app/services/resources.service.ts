import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { Resource } from '../models/resource';
import { PaymentData } from '../models/payment-data';


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
  private static BASE_URL: string = 'http://localhost:42322/api/resource';
  private static headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
  constructor(private http: HttpClient, private auth: AuthService) { }
  getResources(): Observable<Resource[]> {
    return this.http.get<Resource[]>(
      ResourcesService.BASE_URL + "/all",
      { headers: this.auth.authenticatedHeaders(ResourcesService.headers) },
    );
  }
  orderResources(tributeId: number, sponsorId: number, resources: Resource[]): Observable<PaymentData> {
    return this.http.post<PaymentData>(
      ResourcesService.BASE_URL + "/send",
      new SponsorResourceOrderRequest(tributeId, sponsorId, resources), 
      { headers: this.auth.authenticatedHeaders(ResourcesService.headers) },
    );
  }
}
