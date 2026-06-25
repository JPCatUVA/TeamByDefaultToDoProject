import { Routes } from '@angular/router';
import { loginAuthGuard } from './guards/login-auth-guard';
import { guestGuard } from './guards/guest-guard';
import { Login } from './login/login';
import { Home } from './home-view/home';
import { TaskView } from './task-view/task-view';
import { SubtaskView } from './subtask-view/subtask-view';
import { Register } from './register/register';

export const routes: Routes = [
  // Default redirect
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  // Public routes — only accessible when NOT logged in
  { path: 'login', component: Login, canActivate: [guestGuard] },
  { path: 'register', component: Register, canActivate: [guestGuard] },

  // Protected routes — blocked until user is logged in
  { path: 'home', component: Home, canActivate: [loginAuthGuard] },
  { path: 'task/:taskId', component: TaskView, canActivate: [loginAuthGuard] },
  { path: 'task/:taskId/subtask/:subtaskId', component: SubtaskView, canActivate: [loginAuthGuard] },

  // Fallback for unknown paths
  { path: '**', redirectTo: 'login' },
];
