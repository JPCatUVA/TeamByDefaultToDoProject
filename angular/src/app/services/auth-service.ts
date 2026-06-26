import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private router = inject(Router);
  private http = inject(HttpClient);

  private readonly BASE_URL = 'http://localhost:8080';
  private readonly TOKEN_KEY = 'token';
  private readonly USER_ID_KEY = 'userId';

  // POST /register — sends email + password, expects 201 Created
  register(credentials: { email: string; password: string }) {
    return this.http.post<void>(`${this.BASE_URL}/register`, credentials);
  }

  // POST /login — sends email + password, expects a JWT string back
  login(credentials: { email: string; password: string }) {
    return this.http.post(`${this.BASE_URL}/login`, credentials, { responseType: 'text' }).pipe(
      tap((token) => {
        // Store the token so it survives page refreshes
        localStorage.setItem(this.TOKEN_KEY, token);
        // Extract the user UUID from the JWT subject claim and persist it
        const userId = this.extractUserIdFromToken(token);
        if (userId) {
          this.setUserId(userId);
        }
      })
    );
  }

  // Decodes the JWT payload and returns the `sub` claim (userId), or null on any error
  private extractUserIdFromToken(token: string): string | null {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.sub ?? null;
    } catch {
      return null;
    }
  }

  // Clear stored credentials and go back to login
  logout() {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_ID_KEY);
    this.router.navigate(['/login']);
  }

  // Used by AuthGuard to check if the user is authenticated
  checkIfUserIsLoggedIn(): boolean {
    return this.getToken() !== null;
  }

  // Returns the raw JWT string, or null if not logged in
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  // Returns the logged-in user's ID, or null if not set
  getUserId(): string | null {
    return localStorage.getItem(this.USER_ID_KEY);
  }

  // Call this after login if your backend returns a userId separately
  setUserId(userId: string) {
    localStorage.setItem(this.USER_ID_KEY, userId);
  }
}
