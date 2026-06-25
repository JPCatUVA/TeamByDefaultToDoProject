import { Routes } from '@angular/router';
import { loginAuthGuard } from './guards/login-auth-guard';

export const routes: Routes = [
  // Default redirect
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  // Public routes — no guard needed
  // { path: 'login',    component: LoginComponent },
  // { path: 'register', component: RegisterComponent },

  // Protected routes — blocked until user is logged in
  // { path: 'todos',          component: TodoListComponent,   canActivate: [loginAuthGuard] },
  // { path: 'todos/:taskId',  component: TodoDetailComponent, canActivate: [loginAuthGuard] },

  // Fallback for unknown paths
  { path: '**', redirectTo: 'login' },
];
