package com.subscription.server.service;

import com.subscription.server.DTO.ServerDTO;
import com.subscription.server.DTO.UserDTO;
import com.subscription.server.model.ServerDAO;
import com.subscription.server.model.UserDAO;
import com.subscription.server.repository.ServerRepository;
import com.subscription.server.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;

    public UserDTO getUser(int id)
    {
        try
        {
            UserDAO usr = userRepository.findById(id).orElse(null);
            return modelMapper.map(usr,UserDTO.class);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }
    public UserDTO addUser(UserDTO user)
    {
        try
        {
            UserDAO newUser = modelMapper.map(user,UserDAO.class);
            userRepository.save(newUser);
            return user;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }
    public void deleteUser( int id )
    {
      //  userRepository.deleteById(modelMapper.map(usr,UserDAO.class).getId());
        try
        {
            userRepository.deleteById(id);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    public UserDTO updateUser(UserDTO user)
    {
        UserDAO toBeUpdatedUser = modelMapper.map(user,UserDAO.class);
        try
        {
            userRepository.save(toBeUpdatedUser);
            return user;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

}
