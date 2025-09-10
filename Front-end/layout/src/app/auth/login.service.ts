import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { jwtDecode } from "jwt-decode";
import { Login } from '../models/login'; 
import { Usuario } from '../auth/usuario'; 

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  http = inject(HttpClient);
  API = "http://localhost:8080/api/login";

  logar(login: Login): Observable<string> {
    return this.http.post<string>(this.API, login, { responseType: 'text' as 'json' });
  }

  addToken(token: string) {
    localStorage.setItem('token', token);
  }

  removerToken() {
    localStorage.removeItem('token');
  }

  getToken() {
    return localStorage.getItem('token');
  }

  isLoggedIn(): boolean {
    return this.getToken() != null;
  }

  jwtDecode(): Usuario | null {
    const token = this.getToken();
    if (token) {
      try {
        return jwtDecode<Usuario>(token);
      } catch (e) {
        console.error("Token inválido ou expirado", e);
        this.removerToken(); 
        return null;
      }
    }
    return null;
  }

  hasRole(role: string): boolean {
    const user = this.jwtDecode();
    return user != null && user.role === role;
  }

  getUsuarioLogado(): Usuario | null {
    return this.jwtDecode();
  }
}