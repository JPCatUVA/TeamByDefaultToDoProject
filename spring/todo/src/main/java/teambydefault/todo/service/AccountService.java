package teambydefault.todo.service;

import teambydefault.todo.entity.Account;
import teambydefault.todo.exception.RegistrationException;
import teambydefault.todo.repo.AccountRepo;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountService {
    
    private final AccountRepo accRepo;

    /*
        Implement some registration rules here?
        Email Rules:
        - Not Null
        - A valid email must have "@" character

        Password Rules:
        - Not Null 
        - 5-15 characters long (Suggest different ranges?)
        - Have at least one uppercase letter, one lowercase letter and one special character
    */

    //Based on demo: Will definitely need to be changed 
    //for email instead of username
    public void registerAcc(Account acc) {
        // call helper methods here

        // check email validity
        if(!isNotNull(acc.getEmail())) {
            //Use a custom exception in place of this line here
            throw new RegistrationException("Email should mot be empty.");
        }
        // I don't think emails have a length restriction...
        // if(!isCorrectLength(acc.getEmail())) {
        //     throw new RegistrationException("Placeholder error for registration.");
        // }

        if(!isUnique(acc.getEmail())) {
            throw new RegistrationException("Email already exists.");  
        }

        //Check password validity
        if(!isNotNull(acc.getPassword())) {
            throw new RegistrationException("Password should not be empty.");
        }
        if(!isCorrectLength(acc.getPassword())) {
            throw new RegistrationException("Password must be between 5-15 characters long.");
        }
        if(!hasCorrectChars(acc.getPassword())) {
            throw new RegistrationException("Password must have at least one special character.");
        }

        //when everything is valid
        accRepo.save(acc);
    }

    //Some sample helper methods based on instuctor demo
    public boolean isCorrectLength(String credential){
        // username is between 5-15 characters long
        return 5 <= credential.length() && credential.length() <= 15;
    }

    public boolean isNotNull(String credential){
        return credential != null;
    }

    //There should be something more ideal here?
    public boolean hasCorrectChars(String credential){
        boolean hasLowercase = false;
        boolean hasUppercase = false;
        boolean hasDigit = false;
        //boolean hasEmailSymbol = false;

        for (char c : credential.toCharArray()) {
            if (Character.isLowerCase(c)) hasLowercase = true;
            if (Character.isUpperCase(c)) hasUppercase = true;
            if (Character.isDigit(c)) hasDigit = true;
            //if (c == '@') hasEmailSymbol = true;
            if (hasLowercase && hasUppercase && hasDigit) return true;
        }
        
        return false;
    }

    public boolean isUnique(String credential) {
        Optional<Account> accOptional = accRepo.findByEmail(credential);
        return !accOptional.isPresent();
    }
}