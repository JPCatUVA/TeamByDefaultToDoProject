import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from './services/auth-service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  private authService = inject(AuthService);

  // used as a signal in case we wanted to change the webpage's title dynamically
  protected readonly title = signal('todo-back-end');

  get isLoggedIn(): boolean {
    return this.authService.checkIfUserIsLoggedIn();
  }

  logout(): void {
    this.authService.logout();
  }
}
