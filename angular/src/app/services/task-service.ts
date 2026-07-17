import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Todo } from '../interfaces/Todo';

@Injectable({
  providedIn: 'root',
})
export class TaskService {
  private http = inject(HttpClient);

  private readonly BASE_URL = 'http://184.73.108.173:8080/task';

  // GET /task?userId={userId}
  getAllByUser(userId: string) {
    return this.http.get<Todo[]>(this.BASE_URL, {
      params: { userId },
    });
  }

  // GET /task/{taskId}
  getById(taskId: string) {
    return this.http.get<Todo>(`${this.BASE_URL}/${taskId}`);
  }

  // POST /task
  create(todo: Partial<Todo>) {
    return this.http.post<Todo>(this.BASE_URL, todo);
  }

  // PATCH /task/{taskId}
  patch(taskId: string, changes: Partial<Todo>) {
    return this.http.patch<Todo>(`${this.BASE_URL}/${taskId}`, changes);
  }

  // DELETE /task/{taskId}
  delete(taskId: string) {
    return this.http.delete<void>(`${this.BASE_URL}/${taskId}`);
  }
}
