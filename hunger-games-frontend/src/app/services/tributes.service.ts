import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { Tribute } from '../models/tribute';
import { PaymentData } from '../models/payment-data';

@Injectable({
  providedIn: 'root'
})
export class TributesService {
  private static BASE_URL: string = 'http://localhost:42322/api/tribute';
  private static headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
  constructor(private http: HttpClient, private auth: AuthService) { }
  getTributes(): Observable<Tribute[]> {
    return this.http.get<Tribute[]>(
      TributesService.BASE_URL + "/all",
      { headers: this.auth.authenticatedHeaders(TributesService.headers) },
    );
  }
  orderAd(tributeId: number, text: string): Observable<PaymentData> {
    return this.http.post<PaymentData>(
      TributesService.BASE_URL + "/advertisement",
      { tributeId, text },
      { headers: this.auth.authenticatedHeaders(TributesService.headers) },
    );
  }
}
