package com.subscription.server.controller;


import com.subscription.server.DTO.ServerDTO;
import com.subscription.server.DTO.UserDTO;
import com.subscription.server.service.ServerService;
import com.subscription.server.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    ServerService  serverService ;
    @GetMapping("/{id}")
    public ResponseEntity getCustomer(@PathVariable int id)
    {
        UserDTO userDTO = userService.getUser(id);
        if (userDTO == null)
        {
            return new ResponseEntity("This user Does not Exist", HttpStatus.BAD_REQUEST);
        }
        else
        {
            return new ResponseEntity(userDTO, HttpStatus.OK);
        }
    }
    @PostMapping("/")
    public ResponseEntity addUser(@RequestBody UserDTO user){
        try
        {
            userService.addUser(user);
            return new ResponseEntity(user,HttpStatus.OK);
        }
        catch (Exception e)
        {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return new ResponseEntity("operation failed ",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @DeleteMapping("/")
    public ResponseEntity deleteUser(@PathVariable int id )
    {
        try
        {
            userService.deleteUser(id);
            return new ResponseEntity("user deleted successfully ",HttpStatus.OK);
        }
        catch (Exception e )
        {
            System.out.println(e.getStackTrace());
            return new ResponseEntity("operation failed ",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PutMapping("/")
    public ResponseEntity updateUser(UserDTO user)
    {
       if(user == null)
       {
            return new ResponseEntity("please insert a valid user",HttpStatus.BAD_REQUEST);
       }
       try
       {
            userService.updateUser(user);
            return new ResponseEntity(user,HttpStatus.OK);
       }
       catch (Exception e )
       {
           System.out.println(e.getStackTrace());
           return new ResponseEntity("operation failed ",HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }
    @GetMapping("subscribe/{id}")
    public ResponseEntity subscribeToServer(@PathVariable int id , @RequestBody() int capacity)
    {
        try {
            ServerDTO server = serverService.subscribe(id,capacity);
            return new ResponseEntity(server,HttpStatus.OK);
        }
        catch (Exception e )
        {
            System.out.println(e.getStackTrace());
            return new ResponseEntity("allocation failed",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
