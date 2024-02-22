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
  email: string = '';
  password: string = '';

  constructor(private http:HttpClient, private router: Router, private authService: AuthService) { }

  goToRegistration() {
    this.router.navigate(['/register']);
  }

  login() {

    if (!this.email.trim() || !this.password.trim()) {
      this.errorMessage = 'Incorrect Email or Password.';
      return;
    }
  
    this.authService.login(this.email, this.password).subscribe( response => {
        console.log('Login successful', response);
        this.authService.setToken(response.token);
        this.authService.setUserEmail(this.email);
        this.router.navigate(['/tasks']);
      },
      error => {
        console.log('Login failed', error);
          this.errorMessage = 'Incorrect Email or Password.';
        }      
    )
  }

}
