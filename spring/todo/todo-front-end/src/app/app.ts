import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

//Component imports
//import { Login } from '.login/login';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],//, Login],
  templateUrl: './app.html',
  styleUrl: './app.css'
})

export class App {
  //used as a signal in case we wanted to change the webpages title dyamically 
  //at some point
  protected readonly title = signal('todo-back-end');
}
