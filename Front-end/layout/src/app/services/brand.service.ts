import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Brand } from '../models/brand';

@Injectable({
  providedIn: 'root'
})
export class BrandService {

  private http = inject(HttpClient);

  private API = "http://localhost:8080/api/brand";

  constructor() { }

  listAll(): Observable<Brand[]> {
    return this.http.get<Brand[]>(`${this.API}/findAll`);
  }

  findById(id: number): Observable<Brand> {
    return this.http.get<Brand>(`${this.API}/findById/${id}`);
  }

  create(brand: Brand): Observable<Brand> {
    return this.http.post<Brand>(`${this.API}/create`, brand);
  }
 
  delete(id: number): Observable<string> {
    return this.http.delete(`${this.API}/delete/${id}`, { responseType: 'text' });
  }

}