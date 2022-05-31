import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { Resource } from '../models/resource';

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
}
