package teambydefault.todo.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import teambydefault.todo.entity.User;
import teambydefault.todo.exception.RegistrationException;
import teambydefault.todo.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService accService;

    @PostMapping("/register")//May change based on documentation...
    public ResponseEntity<Void> registerNewAcc(@RequestBody User acc) {
        accService.registerAcc(acc);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    
    //Not Universal, triggered by Registration failures from UserService
    @ExceptionHandler(RegistrationException.class)//Change Error handling for custom ones...
    public ResponseEntity<String> handleRegistFail(RegistrationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
