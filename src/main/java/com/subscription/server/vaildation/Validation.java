package com.subscription.server.vaildation;

import com.subscription.server.DTO.UserDTO;
import com.subscription.server.Error.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Validation {
    Error error = new Error();
    public ResponseEntity validateUser(UserDTO newUSer){
        if(newUSer==null){
            return new ResponseEntity<>(error.NullUser(),HttpStatus.BAD_REQUEST);
        }
        if(newUSer.getName().length()<3){
            return new ResponseEntity(error.invalidName(),HttpStatus.BAD_REQUEST);
        }
        if(newUSer.getId()<0){
            return new ResponseEntity<>(error.invalidId(),HttpStatus.BAD_REQUEST);
        }
        if(!(newUSer.getEmail().contains("@")||newUSer.getEmail().contains("."))){
            return new ResponseEntity<>(error.invalidEmail(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(true,HttpStatus.OK);
    }
}
