import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';

//This will be the main view of the application
//It will display a list of all the logged in users tasks
//and allow them to bring one to the forefront

//DOM adjustments are handled through RxJs Observables



@Component({
  selector: 'app-home',
  imports: [ReactiveFormsModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {

}
