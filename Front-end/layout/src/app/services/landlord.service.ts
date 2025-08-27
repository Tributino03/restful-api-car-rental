import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Landlords } from '../models/landlord';
import { Address } from '../models/address';
import { ViaCepDTO } from '../dto/viacep.dto';

@Injectable({
  providedIn: 'root'
})
export class LandlordService {

  private http = inject(HttpClient);
  private API_URL = "http://localhost:8080/api/landlord";
  private CEP_API_URL = "http://localhost:8080/api/address";

  constructor() { }

  findAll(): Observable<Landlords[]> {
    return this.http.get<Landlords[]>(`${this.API_URL}/findAll`);
  }

  findById(id: number): Observable<Landlords> {
    return this.http.get<Landlords>(`${this.API_URL}/findById/${id}`);
  }

  create(landlord: Landlords): Observable<Landlords> {
    return this.http.post<Landlords>(`${this.API_URL}/create`, landlord);
  }

  updateAddress(id: number, address: Address): Observable<Landlords> {
    return this.http.put<Landlords>(`${this.API_URL}/updateAddress/${id}`, address);
  }

  findByCep(cep: string): Observable<ViaCepDTO> {
    return this.http.get<ViaCepDTO>(`${this.CEP_API_URL}/cep/${cep}`);
  }

}