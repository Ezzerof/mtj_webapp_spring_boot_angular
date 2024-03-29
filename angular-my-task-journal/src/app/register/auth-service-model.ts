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

    setUserEmail(email: string): void {
        localStorage.setItem('userEmail', email);
    }
    
    getUserEmail(): string | null {
        return localStorage.getItem('userEmail');
    }
    
    clearUserEmail(): void {
        localStorage.removeItem('userEmail');
    }


    changePassword(currentPassword: string, newPassword: string, email: string): Observable<any> {
        const httpOptions = this.getHttpOptions();
        const payload = { currentPassword, newPassword, email };
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
      
    login(email: string, password: string): Observable<any> {
        return this.http.post<any>(`${this.apiEndpoint}/sign-in`, { email, password })
            .pipe(map((response: any) => {
                this.setToken(response.accessToken);
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
        this._authToken = null;
    }

}