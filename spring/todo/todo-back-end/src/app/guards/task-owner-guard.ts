import { CanActivateFn } from '@angular/router';

export const taskOwnerGuard: CanActivateFn = (route, state) => {
  return true;
};
