import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Car } from '../models/car';

@Injectable({
  providedIn: 'root'
})
export class CarService {
  private apiUrl = 'http://localhost:8080/api/car';

  constructor(private http: HttpClient) {}

  listAll(): Observable<Car[]> {
    return this.http.get<Car[]>(`${this.apiUrl}/findAll`);
  }

  create(carRequest: any): Observable<Car> {
    return this.http.post<Car>(`${this.apiUrl}/create`, carRequest);
  }

  update(car: Car): Observable<Car> {
  return this.http.put<Car>(`${this.apiUrl}/update/${car.id}`, car);
}

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }
}
