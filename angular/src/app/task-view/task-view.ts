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
  editingField: string | null = null;

  // Shared reactive form used for all inline edits
  editForm = this.fb.group({
    title:       ['', Validators.required],
    description: [''],
    dueDate:     ['', Validators.required],
    completed:   [false],
  });

  // Add subtask form
  showAddSubtaskForm = false;
  addSubtaskForm = this.fb.group({
    title:       ['', Validators.required],
    description: [''],
    dueDate:     ['', Validators.required],
  });

  // Keep the current taskId handy for add/delete operations
  private currentTaskId: string | null = null;

  ngOnInit(): void {
    this.data$ = this.route.paramMap.pipe(
      switchMap((params) => {
        const taskId = params.get('taskId');
        this.currentTaskId = taskId;
        if (!taskId) {
          this.errorMessage = 'No task ID provided.';
          return of(null);
        }
        return this.fetchData(taskId);
      }),
      map((data) => data as TaskViewData)
    );
  }

  private fetchData(taskId: string): Observable<TaskViewData | null> {
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
  }

  private refresh(): void {
    if (this.currentTaskId) {
      this.data$ = this.fetchData(this.currentTaskId).pipe(
        map((data) => data as TaskViewData)
      );
    }
  }

  // --- Task field editing ---

  startEdit(field: string, currentValue: unknown): void {
    this.editingField = field;
    this.editForm.patchValue({ [field]: currentValue });
  }

  cancelEdit(): void {
    this.editingField = null;
  }

  saveEdit(task: Todo): void {
    if (!this.editingField) return;

    const field = this.editingField;
    const value = this.editForm.get(field)?.value;

    this.taskService.patch(task.taskId, { [field]: value }).subscribe({
      next: () => {
        this.editingField = null;
        this.refresh();
      },
      error: () => {
        this.errorMessage = 'Failed to save changes. Please try again.';
      },
    });
  }

  // --- Subtask add/delete ---

  toggleAddSubtaskForm(): void {
    this.showAddSubtaskForm = !this.showAddSubtaskForm;
    if (!this.showAddSubtaskForm) this.addSubtaskForm.reset();
  }

  submitAddSubtask(): void {
    if (this.addSubtaskForm.invalid || !this.currentTaskId) return;

    const { title, description, dueDate } = this.addSubtaskForm.value;

    const newSubtask: Partial<Subtask> = {
      title: title!,
      description: description ?? '',
      dueDate: dueDate!,
      completed: false,
      todo: { taskId: this.currentTaskId },
    };

    this.subtaskService.create(newSubtask).subscribe({
      next: () => {
        this.addSubtaskForm.reset();
        this.showAddSubtaskForm = false;
        this.refresh();
      },
      error: () => {
        this.errorMessage = 'Failed to create subtask. Please try again.';
      },
    });
  }

  deleteSubtask(event: MouseEvent, subtaskId: string): void {
    // Prevent click bubbling to the routerLink on the li
    event.stopPropagation();

    this.subtaskService.delete(subtaskId).subscribe({
      next: () => this.refresh(),
      error: () => {
        this.errorMessage = 'Failed to delete subtask. Please try again.';
      },
    });
  }

  goBack(): void {
    this.router.navigate(['/home']);
  }
}
