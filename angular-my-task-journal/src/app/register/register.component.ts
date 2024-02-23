import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from './auth-service-model';
import { AuthenticationResponse } from '../login/authentication-response-model';
import { MatDialog } from '@angular/material/dialog';
import { SensitiveDataConsentDialogComponent } from '../sensitive-data-consent-dialog/sensitive-data-consent-dialog.component';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  @ViewChild('myForm') myForm!: NgForm;

  user = {
    username: '',
    firstName: '',
    middleName: '',
    lastName: '',
    age: '',
    phoneNumber: '',
    email: '',
    password: '',
    courseName: '',
    picture: ''
  }
  passwordTooltip: string = 'Your password must be at least 8 characters long and contain a lowercase letter, an uppercase letter, a number, and a symbol.';

  courses = [
    'Cloud Computing',
    'Business and Management',
    'Computing',
    'Computer Games Design'
  ];

  usernameEmpty = false;
  invalidFirstName = false;
  invalidLastName = false;
  invalidAge = false;
  invalidPhoneNumber = false;
  emailTaken = false;
  passwordTooShort = false;
  errorMessage: string | null = null;

  constructor(private dialog: MatDialog, private http: HttpClient, private router: Router, private authService: AuthService) {}

  goToLogin() {
    this.router.navigate(['/login']);
  }

  ngOnInit(): void {
    this.openConsentDialog();
  }

  openConsentDialog(): void {
    const dialogRef = this.dialog.open(SensitiveDataConsentDialogComponent);

    dialogRef.afterClosed().subscribe(result => {
      if (!result) {
        this.goToLogin();
      }
    });
  }

  onSubmit() {
    
    this.resetErrorState();

    for (const field in this.myForm.controls) {
      const control = this.myForm.controls[field];
      control.markAsTouched({ onlySelf: true });
    }

    if (!this.user.username) {
      this.usernameEmpty = true;
      this.errorMessage = 'Username is required.';
      console.log('Username validation failed');
      return;
    }

    if (!this.user.firstName || !this.user.firstName.match(/^[a-zA-Z]+$/)) {
      this.invalidFirstName = true;
      this.errorMessage = 'Invalid first name. It should only contain alphabets.';
      return;
    }
    if (!this.user.lastName || !this.user.lastName.match(/^[a-zA-Z]+$/)) {
      this.invalidLastName = true;
      this.errorMessage = 'Invalid last name. It should only contain alphabets.';
      return;
    }
  
    if (this.user.password.length < 8) {
      this.passwordTooShort = true;
      this.errorMessage = 'Password is too short. It should be at least 8 characters long.';
      console.log('Password validation failed');
      return;
    }

    let age = parseInt(this.user.age);
    if (isNaN(age) || age < 16 || age > 100) {
      this.invalidAge = true;
      this.errorMessage = 'Age must be between 16 and 100.';
      return;
    }

    if (!this.user.phoneNumber.match(/^\d{10}$/)) {
      this.invalidPhoneNumber = true;
      this.errorMessage = 'Phone number must be 10 digits.';
      return;
    }
  
    this.http.post<AuthenticationResponse>('http://localhost:8080/api/v1/auth/sign-up', this.user)
  .subscribe(
      response => {
        console.log('User object before POST: ', this.user);
        console.log('User registered', response);
        this.router.navigate(['/login']);
      },
      error => {
        console.log('Registration failed', error);
        this.handleError(error);
      }
    );
  }
  
  private resetErrorState() {
    this.usernameEmpty = false;
    this.invalidFirstName = false;
    this.invalidLastName = false;
    this.invalidAge = false;
    this.invalidPhoneNumber = false;
    this.emailTaken = false;
    this.passwordTooShort = false;
    this.errorMessage = null;
  }
  
  private handleError(error: any) {
    if (error.error instanceof ErrorEvent) {
      this.errorMessage = `Error: ${error.error.message}`;
      return;
    }
  
    this.errorMessage = `Error Code: ${error.status}\nMessage: ${(error.error && error.error.message) ? error.error.message : 'Fields cannot be empty.'}`;
  
    if (error.status === 409) { 
      const errorDetails = error.error;
  
      if (errorDetails && errorDetails.message) {
        if (errorDetails.message.includes('Username')) {
          this.usernameEmpty = true;
        } else if (errorDetails.message.includes('Email')) {
          this.emailTaken = true;
        }
      }
    }
  }
}
