package teambydefault.todo.service;

import teambydefault.todo.entity.User;
import teambydefault.todo.exception.RegistrationException;
import teambydefault.todo.repo.UserRepo;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    
    private final UserRepo accRepo;

    /*
        Implement some registration rules here?
        Email Rules:
        - Not Null
        - A valid email must have "@" character

        Password Rules:
        - Not Null 
        - 5-15 characters long (Suggest different ranges?)
        - Have at least one uppercase letter, one lowercase letter, one number and one special character
    */

    //Based on demo: Will definitely need to be changed 
    //for email instead of username
    public void registerAcc(User acc) {
        
        //Helper methods
        // check email validity
        if(!isNotNull(acc.getEmail())) {
            //Use a custom exception in place of this line here
            throw new RegistrationException("Email should mot be empty.");
        }
        
        if(!isValidEmail(acc.getEmail())) {
            throw new RegistrationException("This is not a valid email format.");
        }

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
        // password could be between 5-15 characters long
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
        boolean hasSpecialChar = false;

        for (char c : credential.toCharArray()) {
            if (Character.isLowerCase(c)) hasLowercase = true;
            if (Character.isUpperCase(c)) hasUppercase = true;
            if (Character.isDigit(c)) hasDigit = true;
            if (!Character.isLetterOrDigit(c)) hasSpecialChar = true;
            if (hasLowercase && hasUppercase && hasDigit && hasSpecialChar) return true;
        }
        
        return false;
    }

    //Should verify that's a valid email format
    public boolean isValidEmail(String credential) {
        int charIndex = credential.indexOf("@");

        if (charIndex <= 0) return false; //String starting with just the "@" should not be valid
        if (charIndex == credential.length()) return false; //Should count out strings that only have "@"
        if (credential.indexOf("@", charIndex + 1) != -1) return false; //Ensures no repeating "@" in the string

        return true;
    }

    public boolean isUnique(String credential) {
        Optional<User> accOptional = accRepo.findByEmail(credential);
        return !accOptional.isPresent();
    }

    /**
     * Attempts login for the given email/password.
     * Returns:
     *   "ok"             - credentials match
     *   "not_found"      - no account with that email
     *   "wrong_password" - email found but password doesn't match
     *   "error"          - unexpected failure
     */
    public String login(String email, String password) {
        if (email == null || password == null) return "error";

        Optional<User> userOpt = accRepo.findByEmail(email);
        if (userOpt.isEmpty()) return "not_found";

        User user = userOpt.get();
        if (!user.getPassword().equals(password)) return "wrong_password";

        return "ok";
    }
}