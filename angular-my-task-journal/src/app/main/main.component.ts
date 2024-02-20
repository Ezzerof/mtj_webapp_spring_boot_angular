import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../register/auth-service-model';
import { Router } from '@angular/router';
import { TaskService } from './task.service';
import { Task } from './task.interface';
import { formatDate } from '@angular/common';


@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit{
  today = '';
  minDueDateTime = '';
  tasks: Task[] = [];
  showNewTaskForm: boolean = false;
  newTask: Task = { 
    taskName: '',
    description: '',
    dueDateTime: '',
    startDateTime: '',
    status: 'TO_DO'
  };

  isEditing: boolean = false;
  editedTask: Task | null = null;
  originalTask: Task | null = null;
  isDateInvalid: boolean = false;
  sortBy: string = 'taskName';
  direction: string = 'asc';


  constructor(private authService: AuthService, private router: Router, private taskService: TaskService) { }

  displayStatus(status: string): string {
    switch (status) {
      case 'TO_DO':
        return 'To Do';
      case 'IN_PROGRESS':
        return 'In Progress';
      case 'DONE':
        return 'Done';
      default:
        return status;
    }
  }

  logout() {
    this.authService.clearToken();
    this.router.navigate(['/login']);
  }

  changePassword() {
    const currentPassword = prompt('Please enter your current password: ');
    if(!currentPassword) {
      alert('Password chage cancelled.');
      return;
    }

    const newPassword = prompt('Please enter your new password:');
    if (!newPassword) {
      alert('Password change cancelled.');
      return;
    }

    this.authService.changePassword(currentPassword, newPassword).subscribe({
      next: (response) => {
        alert('Password successfully changed.');
        this.logout();
      },
      error: (error) => {
        console.error('There was an error changing the password', error);
        alert('Failed to change password.');
      }
    });
  }

  deleteAccount() {
    if (confirm('Are you sure you want to delete your account? This cannot be undone.')) {
        this.authService.deleteAccount().subscribe({
            next: () => {
                alert('Account successfully deleted.');
                this.logout();
            },
            error: (error) => {
                console.error('Error deleting account:', error);
                alert('Failed to delete account.');
            }
        });
    }
}

  toggleNewTaskForm() {
    this.showNewTaskForm = !this.showNewTaskForm;
  }

  ngOnInit(): void {
    this.today = formatDate(new Date(), 'yyyy-MM-ddTHH:mm', 'en');
    this.minDueDateTime = this.today;
    this.loadTasks();
  }

  onStartDateChange(): void {
    this.minDueDateTime = this.newTask.startDateTime;
  }
  
  loadTasks() {
    this.taskService.getTasks(this.sortBy, this.direction).subscribe(
      (tasks) => {
        this.tasks = tasks;
      },
      (error) => {
        console.error('Error retrieving tasks:', error);
      }
    );
  }

  private resetNewTaskForm() {
    this.newTask = {taskName: '', description: '', dueDateTime: '', startDateTime: '', status: 'TO_DO' };
    this.showNewTaskForm = false;
  }
  

  addTask() {

    if (!this.newTask.taskName.trim() || !this.newTask.description.trim() || 
        !this.newTask.startDateTime || !this.newTask.dueDateTime) {
      console.error('All fields must be filled out.');
      return;
    }

    if (new Date(this.newTask.dueDateTime) < new Date(this.newTask.startDateTime)) {
      console.error('Due date and time must be after the start date.');
      this.isDateInvalid = true; 
      return;
    } else {
      this.isDateInvalid = false;
    }
  
    this.taskService.addTask(this.newTask).subscribe(
      (task) => {
        if (task) {
          this.tasks.push(task);
        } else {
          console.error('Task received from backend is null');
        }
        this.resetNewTaskForm();
        this.loadTasks();
      },
      (error) => {
        console.error('There was an error adding the task', error);
      }
    );
  }
  

  deleteTask(taskId: number | undefined) {
    if (typeof taskId === 'undefined') {

      console.error('Cannot delete task without an ID.');
      return;
    }
    
    if (confirm('Are you sure you want to delete this task?')) {
      this.taskService.deleteTask(taskId).subscribe(
        () => {

          this.tasks = this.tasks.filter(task => task.taskId !== taskId);
        },
        (error) => {

          console.error('Error deleting task:', error);
        }
      );
    }
  }

  updateTask(taskId: number | undefined) {

  }

  editTask(task: Task) {
    this.isEditing = true;
    this.editedTask = { ...task };
    this.originalTask = { ...task };
  }

  saveTask() {
    if (!this.editedTask || this.editedTask.taskId === undefined) {
      console.error('Cannot save task without valid data.');
      return;
    }

    const taskId = this.editedTask.taskId as number;

    const taskToUpdate: Task = {
      ...this.editedTask,
      taskId: taskId 
    };

    console.log(taskToUpdate);
  
    this.taskService.updateTask(taskId, taskToUpdate).subscribe(
      (updatedTask) => {
        const index = this.tasks.findIndex(t => t.taskId === taskId);
        if (index !== -1) {

          this.tasks[index] = {...this.tasks[index], ...updatedTask};
          this.isEditing = false;
          this.editedTask = null;
        } else {
          console.error('Task to update was not found in the local array.');
        }
      },
      (error) => {
        console.error('There was an error updating the task', error);
      }
    );
  }
  

  cancelEdit() {
    this.isEditing = false;
    this.editedTask = null;

    if (this.originalTask) {
      const index = this.tasks.findIndex((t) => t.taskId === this.originalTask!.taskId);
      if (index !== -1) {
        this.tasks[index] = { ...this.originalTask };
      }
      this.originalTask = null;
    }
  }

}