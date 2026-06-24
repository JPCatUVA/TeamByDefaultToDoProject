import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { taskOwnerGuardGuard } from './task-owner-guard-guard';

describe('taskOwnerGuardGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => taskOwnerGuardGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
