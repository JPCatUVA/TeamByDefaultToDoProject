import { CanActivateFn } from '@angular/router';

export const loginAuthGuard: CanActivateFn = (route, state) => {
  return true;
};
