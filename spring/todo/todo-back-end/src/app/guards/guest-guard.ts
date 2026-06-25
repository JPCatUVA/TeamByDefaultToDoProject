import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth-service';

export const guestGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.checkIfUserIsLoggedIn()) {
    return true;
  }

  // Already logged in — redirect to todos instead
  return router.createUrlTree(['/todos']);
};
