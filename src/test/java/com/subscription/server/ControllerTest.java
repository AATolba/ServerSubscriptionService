package com.subscription.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subscription.server.DTO.UserDTO;
import com.subscription.server.controller.UserController;
import com.subscription.server.service.ServerService;
import com.subscription.server.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class ControllerTest {
    @Autowired
    private UserController userController;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    UserService userService;
    @MockBean
    ServerService serverService;

    @Test
    public void GetUserTest() throws Exception
    {
        UserDTO userDTO = new UserDTO(1,"Ali","ali@gmail.com");
        when(userService.getUser(anyInt())).thenReturn(new ResponseEntity(userDTO, HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Ali"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("ali@gmail.com"))
                .andExpect(status().isOk());
    }
    @Test
    public void AddUserTest() throws Exception {
        UserDTO userDTO = new UserDTO(15,"Ahmed","ahmed@gmail.com");
        when(userService.addUser(any(UserDTO.class))).thenReturn(new ResponseEntity(userDTO,HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders.post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Ahmed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("ahmed@gmail.com"));

    }
    @Test
    public void updateUserTest() throws Exception {
        UserDTO userDTO = new UserDTO(15,"Ali","ahmed@gmail.com");
        when(userService.addUser(any(UserDTO.class))).thenReturn(new ResponseEntity(userDTO,HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders.put("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO))).andDo(print());
    }
}
