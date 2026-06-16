package teambydefault.todo.service;

import teambydefault.todo.entity.Account;
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
    */

    //Based on demo: Will definitely need to be changed 
    //for email instead of username
    public void registerAcc(Account acc) throws RuntimeException {
        // call helper methods here

        // check email validity
        if(!isNotNull(acc.getEmail())) {
            //Use a custom exception in place of this line here
            throw new RuntimeException("Placeholder error for registration.");
        }
        if(!isCorrectLength(acc.getEmail())) {
            //Use a custom exception in place of this line here
            throw new RuntimeException("Placeholder error for registration.");
        }
        if(!isUnique(acc.getEmail())) {
            //Use a custom exception in place of this line here
            throw new RuntimeException("Placeholder error for registration.");  
        }

        //Check password validity
        if(!isNotNull(acc.getPassword())) {
            //Use a custom exception in place of this line here
            throw new RuntimeException("Placeholder error for registration.");
        }
        if(!isCorrectLength(acc.getPassword())) {
            //Use a custom exception in place of this line here
            throw new RuntimeException("Placeholder error for registration.");
        }
        if(!hasCorrectChars(acc.getPassword())) {
            //Use a custom exception in place of this line here
            throw new RuntimeException("Placeholder error for registration.");
        }

        //when evrything is valid
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
        boolean hasEmailSymbol = false;

        for (char c : credential.toCharArray()) {
            if (Character.isLowerCase(c)) hasLowercase = true;
            if (Character.isUpperCase(c)) hasUppercase = true;
            if (Character.isDigit(c)) hasDigit = true;
            if (c == '@') hasEmailSymbol = true;
            if (hasLowercase && hasUppercase && hasDigit && hasEmailSymbol) return true;
        }
        
        return false;
    }

    public boolean isUnique(String credential) {
        Optional<Account> accOptional = accRepo.findByEmail(credential);
        return !accOptional.isPresent();
    }
}