import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { combineLatest, Observable, of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';

import { Todo } from '../interfaces/Todo';
import { Subtask } from '../interfaces/Subtask';
import { TaskService } from '../services/task-service';
import { SubtaskService } from '../services/subtask-service';

// Bundles the task and its subtasks into a single object for the template
interface TaskViewData {
  task: Todo;
  subtasks: Subtask[];
}

@Component({
  selector: 'app-task-view',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './task-view.html',
  styleUrl: './task-view.css',
})
export class TaskView implements OnInit {

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private taskService = inject(TaskService);
  private subtaskService = inject(SubtaskService);
  private fb = inject(FormBuilder);

  // Observable that emits the task and its subtasks — consumed by the template via async pipe
  data$!: Observable<TaskViewData>;
  errorMessage: string | null = null;

  // Tracks which field is currently open for editing — only one field editable at a time
  // Possible values: 'title' | 'description' | 'dueDate' | 'completed' | null
  editingField: string | null = null;

  // Shared reactive form used for all inline edits — only the relevant control is active at a time
  editForm = this.fb.group({
    title: ['', Validators.required],
    description: [''],
    dueDate: ['', Validators.required],
    completed: [false],
  });

  ngOnInit(): void {
    // Read the taskId from the route, then fetch the task and its subtasks in parallel
    this.data$ = this.route.paramMap.pipe(
      switchMap((params) => {
        const taskId = params.get('taskId');
        if (!taskId) {
          this.errorMessage = 'No task ID provided.';
          return of(null);
        }

        // combineLatest fires when both requests complete, emitting both results together
        return combineLatest([
          this.taskService.getById(taskId),
          this.subtaskService.getAllByTask(taskId),
        ]).pipe(
          map(([task, subtasks]) => ({ task, subtasks })),
          catchError(() => {
            this.errorMessage = 'Failed to load task. Please try again.';
            return of(null);
          })
        );
      }),
      // Null is handled via errorMessage — cast here so the template type stays clean
      map((data) => data as TaskViewData)
    );
  }

  // Open the inline editor for a specific field, pre-populated with the current value
  startEdit(field: string, currentValue: unknown): void {
    this.editingField = field;
    this.editForm.patchValue({ [field]: currentValue });
  }

  // Close the editor without saving
  cancelEdit(): void {
    this.editingField = null;
  }

  // Send the edited field value to the backend via PATCH, then refresh the data stream
  saveEdit(task: Todo): void {
    if (!this.editingField) return;

    const field = this.editingField;
    const value = this.editForm.get(field)?.value;

    this.taskService.patch(task.taskId, { [field]: value }).subscribe({
      next: () => {
        this.editingField = null;
        // Re-assign data$ to trigger a fresh fetch after the save
        this.data$ = combineLatest([
          this.taskService.getById(task.taskId),
          this.subtaskService.getAllByTask(task.taskId),
        ]).pipe(
          map(([t, subtasks]) => ({ task: t, subtasks })),
          catchError(() => {
            this.errorMessage = 'Failed to reload task after save.';
            return of(null as unknown as TaskViewData);
          })
        );
      },
      error: () => {
        this.errorMessage = 'Failed to save changes. Please try again.';
      },
    });
  }

  // Navigate back to the home task list
  goBack(): void {
    this.router.navigate(['/home']);
  }
}
