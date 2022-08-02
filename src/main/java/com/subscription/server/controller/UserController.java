package com.subscription.server.controller;


import com.subscription.server.DTO.ServerDTO;
import com.subscription.server.DTO.UserDTO;
import com.subscription.server.model.ServerDAO;
import com.subscription.server.service.ServerService;
import com.subscription.server.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    ServerService  serverService ;
    @GetMapping("/{id}")
    public ResponseEntity getUSer(@PathVariable int id)
    {
        return userService.getUser(id);
    }
    @PostMapping("/")
    public ResponseEntity addUser(@RequestBody UserDTO user){
        return userService.addUser(user);
    }
    @DeleteMapping("/")
    public ResponseEntity deleteUser(@PathVariable int id )
    {
      return userService.deleteUser(id);
    }
    @PutMapping("/")
    public ResponseEntity updateUser(UserDTO user)
    {
       return userService.updateUser(user);
    }
    @GetMapping("subscribe/{id}")
    public ResponseEntity subscribeToServer(@PathVariable int id , @RequestBody() int capacity) {
            return serverService.subscribe(id,capacity);
    }
    @GetMapping("/delete")
    public void deleteAll(){
        serverService.deleteAll();
    }
    @GetMapping("/all")
    public ArrayList<ServerDAO> getAll(){
        return serverService.getAll();
    }





}
