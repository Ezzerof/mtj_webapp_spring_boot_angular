import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';


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

    setRefreshToken(token: string | null): void {
        if (token) {
            localStorage.setItem('refreshToken', token);
        } else {
            localStorage.removeItem('refreshToken');
        }
    }
      
    getRefreshToken(): string | null {
    return localStorage.getItem('refreshToken');
    }

    refreshToken(): Observable<any> {
        const refreshToken = this.getRefreshToken();
        if (!refreshToken) {
          throw new Error('No refresh token available');
        }
        return this.http.post<any>(`${this.apiEndpoint}/refresh-token`, { refreshToken }, {
          headers: new HttpHeaders({ 'Content-Type': 'application/json' })
        });
      }

    changePassword(currentPassword: string, newPassword: string): Observable<any> {
        const httpOptions = this.getHttpOptions();
        const payload = { currentPassword, newPassword };
        return this.http.post(`${this.apiEndpoint}/change-password`, payload, httpOptions);
    }

    deleteAccount(): Observable<any> {
        const httpOptions = this.getHttpOptions();
        return this.http.delete(`${this.apiEndpoint}/delete-account`, httpOptions)
            .pipe(
                map(response => {
                    return response;
                }),
                catchError(error => {
                    return throwError(() => new Error('Failed to delete the account'));
                })
            );
    }
      
    login(username: string, password: string): Observable<any> {
        return this.http.post<any>(`${this.apiEndpoint}/sign-in`, { username, password })
            .pipe(map((response: any) => {
            this.setToken(response.accessToken);
            this.setRefreshToken(response.refreshToken);
            return response;
        }));
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
        return this._authToken ?? localStorage.getItem('authToken');
    }
    
    clearToken(): void {
        localStorage.removeItem('authToken');
        localStorage.removeItem('refreshToken');
        this._authToken = null;
    }

}