import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { Sponsor } from '../models/sponsor';
import { ApiService } from './api.service';

@Injectable({
    providedIn: 'root'
})
export class SponsorsService {
    private static BASE_URL: string = `${ApiService.baseURL}/sponsor`;
    private static headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
    constructor(private http: HttpClient, private auth: AuthService) { }
    getSponsors(): Observable<Sponsor[]> {
        return this.http.get<Sponsor[]>(
            SponsorsService.BASE_URL + "/all",
            { headers: this.auth.authenticatedHeaders(SponsorsService.headers) },
        );
    }
}
