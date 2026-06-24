import { CanActivateFn } from '@angular/router';

export const loginAuthGuardGuard: CanActivateFn = (route, state) => {
  return true;
};
