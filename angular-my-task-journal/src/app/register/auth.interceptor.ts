// import { Injectable } from '@angular/core';
// import {
//   HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse, HttpResponse
// } from '@angular/common/http';
// import { BehaviorSubject, tap, catchError, filter, Observable, switchMap, take, throwError } from 'rxjs';
// import { AuthService } from './auth-service-model';

// @Injectable()
// export class AuthInterceptor implements HttpInterceptor {
//   private isRefreshing = false;
//   private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

//   constructor(public authService: AuthService) {}

//   intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
//     if (this.authService.getToken()) {
//       request = this.addToken(request, this.authService.getToken());
//     }

//     return next.handle(request).pipe(
//       tap(evt => {
//         if (evt instanceof HttpResponse) {
//           // Success response logging
//           console.log('Response intercepted', evt);
//         }
//       }),
//       catchError((error: HttpErrorResponse) => {
//         console.error('Error intercepted', error);
//         if (error.status === 401) {
//           // Handle 401 errors (unauthorized)
//           return this.handle401Error(request, next);
//         } else if (error.status === 200) {
//           // This block checks if somehow a 200 OK response is treated as an error, 
//           // which should not normally happen. This is just for debugging purposes.
//           console.warn('Received 200 OK as error', error);
//           return throwError(() => new Error('Received 200 OK as error'));
//         } else {
//           // Handle other errors
//           return throwError(() => error);
//         }
//       })
//     );
//   }

//   private addToken(request: HttpRequest<any>, token: string | null) {
//     return request.clone({
//       setHeaders: {
//         'Authorization': `Bearer ${token}`
//       }
//     });
//   }

//   private handle401Error(request: HttpRequest<any>, next: HttpHandler) {
//     console.log('Attempting to refresh token');
//     if (!this.isRefreshing) {
//       this.isRefreshing = true;
//       this.refreshTokenSubject.next(null);

//       return this.authService.refreshToken().pipe(
//         switchMap((token: any) => {
//           this.isRefreshing = false;
//           this.refreshTokenSubject.next(token.jwt);
//           this.authService.setToken(token.jwt);
//           return next.handle(this.addToken(request, token.jwt));
//         }),
//         catchError((error) => {
//           this.isRefreshing = false;
//           this.authService.clearToken(); 
//           return throwError(() => error);
//         })
//       );
//     } else {
//       return this.refreshTokenSubject.pipe(
//         filter(token => token != null),
//         take(1),
//         switchMap(jwt => {
//           return next.handle(this.addToken(request, jwt));
//         })
//       );
//     }
//   }
// }