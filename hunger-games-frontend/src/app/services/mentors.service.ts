import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { Mentor } from '../models/mentor';
import { Order } from '../models/order';

@Injectable({
  providedIn: 'root'
})
export class MentorsService {
  private static BASE_URL: string = 'http://localhost:42322/api/mentor';
  private static headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
  constructor(private http: HttpClient, private auth: AuthService) { }
  getMentors(): Observable<Mentor[]> {
    return this.http.get<Mentor[]>(
      MentorsService.BASE_URL + "/all",
      { headers: this.auth.authenticatedHeaders(MentorsService.headers) },
    );
  }
  getOrders(mentorId: number): Observable<Order[]> {
    return this.http.get<Order[]>(
      MentorsService.BASE_URL + "/orders",
      {
        headers: this.auth.authenticatedHeaders(MentorsService.headers),
        params: { mentorId }
      },
    );
  }
  approve(mentorId: number, orderId: number, approved: boolean): Observable<void> {
    return this.http.post<void>(
      MentorsService.BASE_URL + "/order/approve",
      { mentorId, orderId, approved },
      {
        headers: this.auth.authenticatedHeaders(MentorsService.headers),
      },
    );
  }
}
