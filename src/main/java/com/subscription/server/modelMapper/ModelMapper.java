package com.subscription.server.modelMapper;

import com.subscription.server.DTO.ServerDTO;
import com.subscription.server.DTO.UserDTO;
import com.subscription.server.model.ServerDAO;
import com.subscription.server.model.UserDAO;
import org.springframework.http.ResponseEntity;

public class ModelMapper {
    public ServerDTO serverDAO2DTO(ServerDAO serverDAO){
        return new ServerDTO(serverDAO.getId(),serverDAO.getRemCapacity(),serverDAO.getFullCapacity());
    }
    public ServerDAO serverDTO2DAO(ServerDTO serverDTO){
        return new ServerDAO(serverDTO.getId(),serverDTO.getRemCapacity(),serverDTO.getFullCapacity());
    }
    public UserDTO userDAO2DTO(UserDAO userDAO){
        return new UserDTO(userDAO.getId(),userDAO.getName(),userDAO.getEmail());
    }
    public UserDAO userDTO2DAO(UserDTO userDTO){
        return new UserDAO(userDTO.getId(),userDTO.getName(),userDTO.getEmail());
    }
}
