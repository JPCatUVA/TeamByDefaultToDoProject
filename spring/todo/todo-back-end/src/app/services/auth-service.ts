import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';


@Injectable({
  providedIn: 'root',
})
export class AuthService {
    // if you ever need to control routing in your classes use the Router service
  private router = inject(Router)

  // this is our placeholder for a real authentication token
  private isLoggedIn = false;

  checkIfUserIsLoggedIn(){
    return this.isLoggedIn;
  }

  /*
    Note: in a real login attempt you would want to actually send the user
          credentials to the server and validate them. Here we will just
          toggle theisLoggedIn flag
  */
  attemptLogin(){
    this.isLoggedIn = true;
    this.router.navigate(["home"]);
  }
}
