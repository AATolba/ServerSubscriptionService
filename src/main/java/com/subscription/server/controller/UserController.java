package com.subscription.server.controller;


import com.subscription.server.DTO.ServerRequest;
import com.subscription.server.DTO.UserDTO;
import com.subscription.server.model.ServerDAO;
import com.subscription.server.service.ServerService;
import com.subscription.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    public ResponseEntity deleteUser(@RequestBody UserDTO userDTO )
    {
      return userService.deleteUser(userDTO.getId());
    }
    @PutMapping("/")
    public ResponseEntity updateUser(@RequestBody UserDTO user)
    {
       return userService.updateUser(user);
    }
    @PutMapping("subscribe/{id}")
    public  ResponseEntity subscribeToServer(@PathVariable int id , @RequestBody ServerRequest serverRequest) {
        return serverService.subscribe(id,serverRequest.getRequiredCapacity());
    }
    @DeleteMapping("/delete")
    public void deleteAll(){
        serverService.deleteAll();
    }
    @GetMapping("/all")
    public List<ServerDAO> getAll(){
        return serverService.getAll();
    }


}
