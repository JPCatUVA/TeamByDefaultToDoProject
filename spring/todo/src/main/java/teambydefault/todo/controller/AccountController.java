package teambydefault.todo.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import teambydefault.todo.entity.Account;
import teambydefault.todo.service.AccountService;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accService;

    @PostMapping("/register")//May change based on documentation...
    public ResponseEntity<Void> registerNewAcc(@RequestBody Account acc) {
        accService.registerAcc(acc);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    
    //Not Universal, triggered by Registration failures
    //from AccountService
    @ExceptionHandler(RuntimeException.class)//Change Error handling for custom ones...
    public ResponseEntity<String> handleRegistFail(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
