import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth-service';

// Attaches the JWT token from localStorage to every outgoing HTTP request.
// Requests to /login and /register are public — they don't need a token.
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);

  const publicPaths = ['/login', '/register'];
  const isPublic = publicPaths.some((path) => req.url.endsWith(path));

  if (isPublic) {
    return next(req);
  }

  const token = authService.getToken();

  if (!token) {
    return next(req);
  }

  const authReq = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`,
    },
  });

  return next(authReq);
};
