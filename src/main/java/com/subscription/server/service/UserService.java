package com.subscription.server.service;

import com.subscription.server.DTO.ServerDTO;
import com.subscription.server.DTO.UserDTO;
import com.subscription.server.Error.Error;
import com.subscription.server.Error.LoggingMessages;
import com.subscription.server.model.ServerDAO;
import com.subscription.server.model.UserDAO;
import com.subscription.server.modelMapper.ModelMapper;
import com.subscription.server.repository.ServerRepository;
import com.subscription.server.repository.UserRepository;
import com.subscription.server.vaildation.ValidMessage;
import com.subscription.server.vaildation.Validation;
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
    ValidMessage valid = new ValidMessage();
    Validation validation = new Validation();
    Logger logger = LoggerFactory.getLogger(UserService.class);
    LoggingMessages loggingMessages= new LoggingMessages();
    Error error = new Error();
    ModelMapper modelMapper = new ModelMapper();
    public ResponseEntity  getUser(int id)
    {
        try
        {
            UserDAO usr = userRepository.findById(id).orElse(null);
            logger.info(loggingMessages.returnUser());
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
        if(validation.validateUser(user).getStatusCode()!=HttpStatus.OK)
        {
            return validation.validateUser(user);
        }
        try
        {
            logger.info(loggingMessages.addUser());
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
            logger.info(loggingMessages.deleteUser());
            return new ResponseEntity<>(valid.deleteSuccess(),HttpStatus.OK);
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
            logger.info(loggingMessages.updateUser());
            return new ResponseEntity<>(user,HttpStatus.OK);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            return new ResponseEntity<>(error.InternalServerError(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
