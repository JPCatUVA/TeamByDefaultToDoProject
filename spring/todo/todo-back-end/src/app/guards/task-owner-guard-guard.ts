import { CanActivateFn } from '@angular/router';

export const taskOwnerGuardGuard: CanActivateFn = (route, state) => {
  return true;
};
