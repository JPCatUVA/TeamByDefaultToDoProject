import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { loginAuthGuardGuard } from './login-auth-guard-guard';

describe('loginAuthGuardGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => loginAuthGuardGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
