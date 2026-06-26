import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Observable, of } from 'rxjs';
import { map, catchError, switchMap } from 'rxjs/operators';

import { Todo } from '../interfaces/Todo';
import { TaskService } from '../services/task-service';
import { AuthService } from '../services/auth-service';

@Component({
  selector: 'app-home',
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {

  private tService = inject(TaskService);
  private authService = inject(AuthService);
  private fb = inject(FormBuilder);

  // Todos sorted by dueDate ascending (soonest first)
  tasks$: Observable<Todo[]> = this.loadTasks();

  errorMessage: string | null = null;
  showAddForm = false;

  addForm = this.fb.group({
    title:       ['', Validators.required],
    description: [''],
    dueDate:     ['', Validators.required],
  });

  private loadTasks(): Observable<Todo[]> {
    const userId = this.authService.getUserId();

    if (!userId) {
      this.errorMessage = 'No logged-in user found.';
      return of([]);
    }

    return this.tService.getAllByUser(userId).pipe(
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

  // Refresh the task list by re-assigning the observable
  private refresh(): void {
    this.tasks$ = this.loadTasks();
  }

  toggleAddForm(): void {
    this.showAddForm = !this.showAddForm;
    if (!this.showAddForm) this.addForm.reset();
  }

  submitAdd(): void {
    if (this.addForm.invalid) return;

    const userId = this.authService.getUserId();
    if (!userId) return;

    const { title, description, dueDate } = this.addForm.value;

    const newTask: Partial<Todo> = {
      title: title!,
      description: description ?? '',
      dueDate: dueDate!,
      completed: false,
      user: { userId: userId },
    };

    this.tService.create(newTask).subscribe({
      next: () => {
        this.addForm.reset();
        this.showAddForm = false;
        this.refresh();
      },
      error: () => {
        this.errorMessage = 'Failed to create task. Please try again.';
      },
    });
  }

  deleteTask(event: MouseEvent, taskId: string): void {
    // Stop the click from bubbling up to the li's routerLink
    event.stopPropagation();

    this.tService.delete(taskId).subscribe({
      next: () => this.refresh(),
      error: () => {
        this.errorMessage = 'Failed to delete task. Please try again.';
      },
    });
  }
}
