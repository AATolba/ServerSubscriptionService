package com.subscription.server.service;

import com.subscription.server.DTO.ServerDTO;
import com.subscription.server.model.ServerDAO;
import com.subscription.server.repository.ServerRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@AllArgsConstructor
public class CreateServer implements Runnable{
    public int capacity;
    ServerRepository serverRepository;
    ModelMapper modelMapper;

    public void run() {
        try
        {
            Thread.sleep(20000);

        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        ServerService.creatingServers--;

        if(ServerService.runningOperations.contains(capacity))
        {
            ServerService.runningOperations.remove((Integer) capacity);
        }

    }
}