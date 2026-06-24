import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth-service';
import { TaskService } from '../services/task-service';
import { map, catchError, of } from 'rxjs';

export const taskOwnerGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const taskService = inject(TaskService);
  const router = inject(Router);

  const taskId = route.paramMap.get('taskId');
  const userId = authService.getUserId();

  // If there's no taskId in the route or no logged-in user, block immediately
  if (!taskId || !userId) {
    return router.createUrlTree(['/todos']);
  }

  // Fetch the task and verify the logged-in user owns it
  return taskService.getById(taskId).pipe(
    map((task) => {
      if (task.user.id === userId) {
        return true;
      }
      // Task exists but belongs to someone else
      return router.createUrlTree(['/todos']);
    }),
    catchError(() => {
      // Task not found or server error
      return of(router.createUrlTree(['/todos']));
    })
  );
};
