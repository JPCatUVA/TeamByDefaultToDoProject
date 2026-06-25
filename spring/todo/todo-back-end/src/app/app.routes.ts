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


// import { Routes } from '@angular/router';

// //potential routing path imports, TO BE ADJUSTED LATER
// import { Login } from '.login/login';
// import { Register } from './register/register';
// import { Home } from './home/home';
// import {authGuard } from '/auth/auth.guard';



// export const routes: Routes = [

//     //forwarding from /root path to login
//     {path: '', redirectTo: 'login', pathMatch: 'full'},

//     //path to login component
//     {path: 'login', component: Login},
    
//     //path to register component
//     {path: 'register', component: Register},

//     //path to home page view, with route guard
//     //{path: 'home', component: Home, canActivate: [authGuard]}

//     //path to tasks view, with route guard
//     {path: 'task', component: Task, canActivate: [authGuard]},
    
//     //path to subtask view, with route guard
//     {path: 'subtask', component: Subtask, canActivate: [authGuard]},
// ];
