import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';

import { Subtask } from '../interfaces/Subtask';
import { SubtaskService } from '../services/subtask-service';

@Component({
  selector: 'app-subtask-view',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './subtask-view.html',
  styleUrl: './subtask-view.css',
})
export class SubtaskView implements OnInit {

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private subtaskService = inject(SubtaskService);
  private fb = inject(FormBuilder);

  // Observable of the subtask — consumed by the template via async pipe
  subtask$!: Observable<Subtask>;
  errorMessage: string | null = null;

  // Used by the back button to navigate to the parent task
  parentTaskId: string | null = null;

  // Tracks which field is currently open for editing — only one at a time
  // Possible values: 'title' | 'description' | 'dueDate' | 'isCompleted' | null
  editingField: string | null = null;

  // Shared reactive form used for all inline edits
  editForm = this.fb.group({
    title: ['', Validators.required],
    description: [''],
    dueDate: ['', Validators.required],
    isCompleted: [false],
  });

  ngOnInit(): void {
    this.subtask$ = this.route.paramMap.pipe(
      switchMap((params) => {
        const subtaskId = params.get('subtaskId');
        this.parentTaskId = params.get('taskId');

        if (!subtaskId) {
          this.errorMessage = 'No subtask ID provided.';
          return of(null);
        }

        return this.subtaskService.getById(subtaskId).pipe(
          catchError(() => {
            this.errorMessage = 'Failed to load subtask. Please try again.';
            return of(null);
          })
        );
      }),
      map((subtask) => subtask as Subtask)
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
  saveEdit(subtask: Subtask): void {
    if (!this.editingField) return;

    const field = this.editingField;
    const value = this.editForm.get(field)?.value;

    this.subtaskService.patch(subtask.id, { [field]: value }).subscribe({
      next: () => {
        this.editingField = null;
        // Re-assign subtask$ to trigger a fresh fetch after the save
        this.subtask$ = this.subtaskService.getById(subtask.id).pipe(
          catchError(() => {
            this.errorMessage = 'Failed to reload subtask after save.';
            return of(null as unknown as Subtask);
          })
        );
      },
      error: () => {
        this.errorMessage = 'Failed to save changes. Please try again.';
      },
    });
  }

  // Navigate back to the parent task
  goBack(): void {
    if (this.parentTaskId) {
      this.router.navigate(['/task', this.parentTaskId]);
    } else {
      this.router.navigate(['/home']);
    }
  }
}
