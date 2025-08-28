import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Rental } from '../models/rental';

@Injectable({
  providedIn: 'root'
})
export class RentalService {

  private http = inject(HttpClient);
  private API_URL = "http://localhost:8080/api/rental";

  constructor() { }

  findAll(): Observable<Rental[]> {
    return this.http.get<Rental[]>(`${this.API_URL}/findAll`);
  }

  create(rental: Rental): Observable<Rental> {
    return this.http.post<Rental>(`${this.API_URL}/create`, rental);
  }
}