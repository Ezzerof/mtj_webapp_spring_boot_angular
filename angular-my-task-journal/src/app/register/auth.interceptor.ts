import { Injectable } from '@angular/core';
import {
  HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse, HttpResponse
} from '@angular/common/http';
import { BehaviorSubject, tap, catchError, filter, Observable, switchMap, take, throwError } from 'rxjs';
import { AuthService } from './auth-service-model';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

  constructor(public authService: AuthService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.authService.getToken()) {
      request = this.addToken(request, this.authService.getToken());
    }

    return next.handle(request).pipe(
      tap(evt => {
        if (evt instanceof HttpResponse) {
          console.log('Response intercepted', evt);
        }
      }),
      catchError(error => {
        console.error('Error intercepted', error);
        if (error instanceof HttpErrorResponse && error.status === 401) {
          return this.handle401Error(request, next);
        } else {
          return throwError(() => error);
        }
      })
    );
  }

  private addToken(request: HttpRequest<any>, token: string | null) {
    return request.clone({
      setHeaders: {
        'Authorization': `Bearer ${token}`
      }
    });
  }

  private handle401Error(request: HttpRequest<any>, next: HttpHandler) {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);

      return this.authService.refreshToken().pipe(
        switchMap((token: any) => {
          this.isRefreshing = false;
          this.refreshTokenSubject.next(token.jwt);
          this.authService.setToken(token.jwt);
          return next.handle(this.addToken(request, token.jwt));
        }),
        catchError((error) => {
          this.isRefreshing = false;
          this.authService.clearToken(); 
          return throwError(() => error);
        })
      );
    } else {
      return this.refreshTokenSubject.pipe(
        filter(token => token != null),
        take(1),
        switchMap(jwt => {
          return next.handle(this.addToken(request, jwt));
        })
      );
    }
  }
}