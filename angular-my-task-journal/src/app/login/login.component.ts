import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../register/auth-service-model';
import { AuthenticationResponse } from './authentication-response-model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  errorMessage = '';
  username: string = '';
  password: string = '';

  constructor(private http:HttpClient, private router: Router, private authService: AuthService) { }

  goToRegistration() {
    this.router.navigate(['/register']);
  }

  login() {
    this.authService.login(this.username, this.password).subscribe( response => {
        console.log('Login successful', response);
        this.authService.setToken(response.token);
        this.router.navigate(['/tasks']);
      },
      error => {
        console.log('Login failed', error);
        if(error.error instanceof ErrorEvent) {
          this.errorMessage = `Error: ${error.error.message}`;
        } else {
          if (error.error && error.error.message) {
            this.errorMessage = `Error Code: ${error.status}\nMessage: ${error.error.message}`;
          } else {
            this.errorMessage = `Error Code: ${error.status}\nMessage: An unknown error occurred`;
          }
        }
      }      
    )
  }

}
