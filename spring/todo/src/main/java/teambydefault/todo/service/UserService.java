package teambydefault.todo.service;

import teambydefault.todo.entity.User;
import teambydefault.todo.exception.LoginException;
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

    public void registerAcc(User acc) {
        
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

    public User loginAcc(User acc) {

        // Email must be provided
        if (!isNotNull(acc.getEmail())) {
            throw new LoginException("Email should not be empty.");
        }

        // Password must be provided
        if (!isNotNull(acc.getPassword())) {
            throw new LoginException("Password should not be empty.");
        }

        // Look up by email
        Optional<User> found = accRepo.findByEmail(acc.getEmail());
        if (!found.isPresent()) {
            throw new LoginException("Invalid email or password.");
        }

        // Verify password matches
        if (!found.get().getPassword().equals(acc.getPassword())) {
            throw new LoginException("Invalid email or password.");
        }

        return found.get();
    }

    //Some sample helper methods based on instuctor demo
    public boolean isCorrectLength(String credential){
        // password could be between 5-15 characters long
        return 5 <= credential.length() && credential.length() <= 15;
    }

    public boolean isNotNull(String credential){
        return credential != null;
    }

    // Uses lookaheads to assert each required character class is present somewhere in the string
    public boolean hasCorrectChars(String credential) {
        return credential.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).*$");
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

}