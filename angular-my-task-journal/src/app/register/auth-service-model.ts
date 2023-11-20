import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root',
})

export class AuthService {
    private apiEndpoint = 'http://localhost:8080/api/v1/auth';
    private _authToken : string | null = null;

    constructor(private http: HttpClient) {}

    getHttpOptions() {
        const token = this.getToken();
        const httpOptions = {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            })
        };
        return httpOptions;
    }

    login(username: string, password: string): Observable<any> {
        return this.http.post<any>(
          `${this.apiEndpoint}/sign-in`,
          { username, password }
        );
      }

      setToken(token: string | null): void {
        if (token) {
            localStorage.setItem('authToken', token);
        } else {
            localStorage.removeItem('authToken');
        }
        this._authToken = token;
    }
    
    getToken(): string | null {
        // If _authToken is null, try getting it from localStorage
        return this._authToken ?? localStorage.getItem('authToken');
    }
    
    clearToken(): void {
        localStorage.removeItem('authToken');
        this._authToken = null;
    }

}