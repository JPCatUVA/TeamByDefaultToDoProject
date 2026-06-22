package teambydefault.todo.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import teambydefault.todo.entity.User;
import teambydefault.todo.exception.LoginException;
import teambydefault.todo.exception.RegistrationException;
import teambydefault.todo.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService accService;


    //Handle registration of a User
    @PostMapping("/register")
    public ResponseEntity<Void> registerNewAcc(@RequestBody User acc) {
        accService.registerAcc(acc);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    // Handle login of a User
    @PostMapping("/login")
    public ResponseEntity<User> loginAcc(@RequestBody User acc) {
        User loggedIn = accService.loginAcc(acc);
        return ResponseEntity.ok(loggedIn);
    }


    //Not Universal, triggered by Registration failures from UserService
    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<String> handleRegistFail(RegistrationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // Triggered by login failures from UserService
    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> handleLoginFail(LoginException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
