import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth-service';
import { switchMap } from 'rxjs/operators';

// Mirrors the backend hasCorrectChars() regex:
// must contain at least one lowercase, one uppercase, one digit, one special character
function passwordComplexity(control: AbstractControl): ValidationErrors | null {
  const value: string = control.value ?? '';
  const valid = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^a-zA-Z\d]).*$/.test(value);
  return valid ? null : { complexity: true };
}

@Component({
  selector: 'app-register',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {

  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  registerForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    // Backend: 5-15 chars, uppercase, lowercase, digit, special character
    password: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(15), passwordComplexity]],
  });

  errorMessage: string | null = null;
  isLoading = false;

  onSubmit(): void {
    if (this.registerForm.invalid) return;

    this.isLoading = true;
    this.errorMessage = null;

    const { email, password } = this.registerForm.value;
    const credentials = { email: email!, password: password! };

    // Register first, then immediately log in on success
    this.authService.register(credentials).pipe(
      switchMap(() => this.authService.login(credentials))
    ).subscribe({
      next: () => {
        this.isLoading = false;
        this.router.navigate(['/home']);
      },
      error: () => {
        this.isLoading = false;
        this.errorMessage = 'Registration failed. That email may already be in use — please try a different one.';
      },
    });
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }
}
