import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Car } from '../models/car';

@Injectable({
  providedIn: 'root'
})
export class CarService {

  private http = inject(HttpClient);
  private API = "http://localhost:8080/api/car";

  listAll(): Observable<Car[]> {
    return this.http.get<Car[]>(`${this.API}/findAll`);
  }

  findById(id: number): Observable<Car> {
    return this.http.get<Car>(`${this.API}/findById/${id}`);
  }

  create(car: Car): Observable<Car> {
    return this.http.post<Car>(`${this.API}/create`, car);
  }

  update(car: Car): Observable<Car> {
    return this.http.put<Car>(`${this.API}/update/${car.id}`, car);
  }

  delete(id: number): Observable<string> {
    return this.http.delete(`${this.API}/delete/${id}`, { responseType: 'text' });
  }
}