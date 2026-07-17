import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Subtask } from '../interfaces/Subtask';

@Injectable({
  providedIn: 'root',
})
export class SubtaskService {
  private http = inject(HttpClient);

  private readonly BASE_URL = 'http://184.73.108.173:8080/subtask';

  // GET /subtask?taskId={taskId}
  getAllByTask(taskId: string) {
    return this.http.get<Subtask[]>(this.BASE_URL, {
      params: { taskId },
    });
  }

  // GET /subtask/{subtaskId}
  getById(subtaskId: string) {
    return this.http.get<Subtask>(`${this.BASE_URL}/${subtaskId}`);
  }

  // POST /subtask
  create(subtask: Partial<Subtask>) {
    return this.http.post<Subtask>(this.BASE_URL, subtask);
  }

  // PATCH /subtask/{subtaskId}
  patch(subtaskId: string, changes: Partial<Subtask>) {
    return this.http.patch<Subtask>(`${this.BASE_URL}/${subtaskId}`, changes);
  }

  // DELETE /subtask/{subtaskId}
  delete(subtaskId: string) {
    return this.http.delete<void>(`${this.BASE_URL}/${subtaskId}`);
  }
}
