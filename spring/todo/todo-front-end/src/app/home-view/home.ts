import { Component, inject } from '@angular/core';
import { CommonModule, AsyncPipe } from '@angular/common';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

//This will be the main view of the application
//It will display a list of all the logged in users tasks
//and allow them to bring one to the forefront

//the user should be able to logout from this view

//DOM adjustments are handled through RxJs Observables

import { Todo } from '../interfaces/Todo';
import { TaskService } from '../services/task-service';
import { AuthService } from '../services/auth-service';

@Component({
  selector: 'app-home',
  imports: [CommonModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {

  private tService = inject(TaskService);
  private authService = inject(AuthService);

  // Todos sorted by dueDate ascending (soonest first), exposed as an Observable
  // so the template can use the async pipe directly — no manual subscribe/unsubscribe needed
  tasks$: Observable<Todo[]> = this.loadTasks();

  errorMessage: string | null = null;

  private loadTasks(): Observable<Todo[]> {
    const userId = this.authService.getUserId();

    if (!userId) {
      this.errorMessage = 'No logged-in user found.';
      return of([]);
    }

    return this.tService.getAllByUser(userId).pipe(
      //sorting function for todo list
      map((todos) =>
        [...todos].sort(
          (a, b) => new Date(a.dueDate).getTime() - new Date(b.dueDate).getTime()
        )
      ),
      catchError(() => {
        this.errorMessage = 'Failed to load tasks. Please try again.';
        return of([]);
      })
    );
  }

  logout(): void {
    this.authService.logout();
  }
}
