import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { Mentor } from '../models/mentor';

@Injectable({
  providedIn: 'root'
})
export class MentorsService {
  private static BASE_URL: string = 'http://localhost:42322/api/sponsor';
  private static headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
  constructor(private http: HttpClient, private auth: AuthService) { }
  getMentors(): Observable<Mentor[]> {
    return this.http.get<Mentor[]>(
      MentorsService.BASE_URL + "/all",
      { headers: this.auth.authenticatedHeaders(MentorsService.headers) },
    );
  }
}
