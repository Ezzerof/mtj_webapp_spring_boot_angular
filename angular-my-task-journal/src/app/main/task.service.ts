import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Task } from './task.interface';
import { Observable } from 'rxjs';
import { AuthService } from '../register/auth-service-model';

@Injectable({
    providedIn: 'root'
})

export class TaskService {
    private apiEndpoint = 'http://localhost:8080/api/v1/tasks';

    constructor( private http: HttpClient, private authService: AuthService) {}

    getHttpOptions() {
      const token = this.authService.getToken();
      const httpOptions = {
        headers: new HttpHeaders({
          'Content-Type':  'application/json',
          'Authorization': `Bearer ${token}`
        })
      };
      return httpOptions;
    }

    getTasks(): Observable<Task[]> {
      const options = this.getHttpOptions();
      return this.http.get<Task[]>(this.apiEndpoint, options);
    } 
  
    addTask(task: Task): Observable<Task> {
      console.log('Create task called');
      const options = this.getHttpOptions();
      return this.http.post<Task>(this.apiEndpoint, task, options);
    }

    updateTask(id: number, task: Task): Observable<Task> {
      const options = this.getHttpOptions();
      return this.http.put<Task>(`${this.apiEndpoint}/${id}`, task, options);
    }

    deleteTask(id: number): Observable<any> {
      const options = this.getHttpOptions();
      return this.http.delete(`${this.apiEndpoint}/${id}`, options);
    }
}