import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { Tribute } from '../models/tribute';
import { PagedResponce } from '../models/pagedResponce';

@Injectable({
  providedIn: 'root'
})
export class TributesService {
  private static BASE_URL: string = 'http://localhost:42322/tributes';
  private static headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
  constructor(private http: HttpClient, private auth: AuthService) { }
  getTributes(page: number): Observable<Tribute[]> {
    return this.http.get<PagedResponce<{ tributes: Tribute[] }>>(
      TributesService.BASE_URL,
      { headers: this.auth.authenticatedHeaders(TributesService.headers), params: { page } },
    ).pipe(map((with_embedded: PagedResponce<{ tributes: Tribute[] }>) => with_embedded._embedded.tributes))
  }
}
