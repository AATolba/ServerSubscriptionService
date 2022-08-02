package com.subscription.server.service;

import com.subscription.server.DTO.ServerDTO;
import com.subscription.server.DTO.UserDTO;
import com.subscription.server.Error.Error;
import com.subscription.server.model.ServerDAO;
import com.subscription.server.model.UserDAO;
import com.subscription.server.modelMapper.ModelMapper;
import com.subscription.server.repository.ServerRepository;
import com.subscription.server.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(UserService.class);

    Error error = new Error();
    ModelMapper modelMapper = new ModelMapper();
    public ResponseEntity  getUser(int id)
    {
        try
        {
            UserDAO usr = userRepository.findById(id).orElse(null);
            return new ResponseEntity(modelMapper.userDAO2DTO(usr), HttpStatus.OK);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            return new ResponseEntity<>(error.UserDoesNotExist(),HttpStatus.NOT_FOUND);
        }

    }
    public ResponseEntity addUser(UserDTO user)
    {

        UserDAO newUser = modelMapper.userDTO2DAO(user);
        if(user==null){
            return new ResponseEntity<>(error.NullUser(),HttpStatus.BAD_REQUEST);
        }
        if(newUser.getName().length()<3){
                return new ResponseEntity(error.invalidName(),HttpStatus.BAD_REQUEST);
            }
        if(newUser.getId()<0){
                return new ResponseEntity<>(error.invalidId(),HttpStatus.BAD_REQUEST);
            }
        if(!(newUser.getEmail().contains("@")||newUser.getEmail().contains("."))){
                return new ResponseEntity<>(error.invalidEmail(),HttpStatus.BAD_REQUEST);
            }
        try
        {
            userRepository.save(newUser);
            return new ResponseEntity(user,HttpStatus.ACCEPTED);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            return new ResponseEntity<>(error.USerNotSaved(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    public ResponseEntity deleteUser( int id )
    {
      //  userRepository.deleteById(modelMapper.map(usr,UserDAO.class).getId());
        UserDTO user = modelMapper.userDAO2DTO(userRepository.findById(id).orElse(null));
        if(user== null){
            return new ResponseEntity<>(error.UserDoesNotExist(),HttpStatus.NOT_FOUND);
        }
        try
        {
            userRepository.deleteById(id);
            return new ResponseEntity<>("user deleted successfully",HttpStatus.OK);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
        return new ResponseEntity(error.InternalServerError(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity updateUser(UserDTO user)
    {
        UserDAO toBeUpdatedUser = modelMapper.userDTO2DAO(user);
        if(toBeUpdatedUser==null){
            return new ResponseEntity(error.UserDoesNotExist(),HttpStatus.NOT_FOUND);
        }
        try
        {
            userRepository.save(toBeUpdatedUser);
            return new ResponseEntity<>(user,HttpStatus.OK);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            return new ResponseEntity<>(error.InternalServerError(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
