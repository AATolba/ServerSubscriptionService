package com.subscription.server.service;

import com.subscription.server.DTO.ServerDTO;
import com.subscription.server.model.ServerDAO;
import com.subscription.server.modelMapper.ModelMapper;
import com.subscription.server.repository.ServerRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@AllArgsConstructor
public class CreateServer implements Runnable{
    public int capacity;
    boolean finished;
    @Autowired
    ServerRepository serverRepository;
    volatile ServerService serverService ;
    ModelMapper modelMapper = new ModelMapper();
    static Logger logger = LoggerFactory.getLogger(CreateServer.class);
    public CreateServer(int capacity,ServerRepository serverRepository,ModelMapper modelMapper,ServerService serverService,boolean finished){
        this.capacity = capacity;
        this.serverRepository = serverRepository;
        this.modelMapper  = modelMapper;
        this.serverService = serverService;
        this.finished = finished;
    }
    public void run() {
        try
        {
            ServerService.runningOperations.add(capacity);
            ServerService.creatingServers++;
            Thread.sleep(20000);

        }
        catch (InterruptedException e)
        {
         logger.error(e.getMessage());
        }
        ServerService.creatingServers--;

        if(ServerService.runningOperations.contains(capacity))
        {
            ServerService.runningOperations.remove((Integer) capacity);
        }
        serverService.setFinished(true);
        ServerDTO serverDTO= serverService.allocateServer(capacity);
        if(serverDTO == null)
             serverService.saveServer(null,capacity);

    }

}