package com.subscription.server.service;
import com.subscription.server.DTO.ServerDTO;
import com.subscription.server.DTO.UserDTO;
import com.subscription.server.model.ServerDAO;
import com.subscription.server.model.UserDAO;
import com.subscription.server.repository.ServerRepository;
import com.subscription.server.repository.UserRepository;
import jdk.nashorn.internal.runtime.Context;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ServerService {

    ServerRepository serverRepository;
    ModelMapper modelMapper;
    public static final int CAPACITY = 100;
    static ArrayList<Integer> runningOperations  = new ArrayList<>();
    public static int creatingServers ;
    public static int currentId;
    private ArrayList<ServerDTO> getAllServers()
    {
        try
        {
            ArrayList<ServerDAO> serversDAO = (ArrayList<ServerDAO>) serverRepository.findAll();
            return (ArrayList<ServerDTO>) serversDAO.stream().map(server -> modelMapper.map(server,ServerDTO.class));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    private ServerDTO getServer(int serverId)
    {
        try
        {
            return modelMapper.map(serverRepository.findById(serverId),ServerDTO.class);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    private boolean checkForCreatingServers(int requestedCapacity)
    {
        if(runningOperations.size()>0)
        {
            int sum = (int) runningOperations.stream().mapToInt(x -> x).summaryStatistics().getSum();
            if (CAPACITY * creatingServers - (requestedCapacity + sum) >= 0)
            {
                int currentCount = creatingServers;
                while (creatingServers == currentCount) ;
                sum = (int) runningOperations.stream().mapToInt(x -> x).summaryStatistics().getSum();
                if (CAPACITY * creatingServers - (requestedCapacity + sum) >= 0)
                {
                    checkForCreatingServers(requestedCapacity);
                }
            }
        }
        return false;
    }
    private ServerDTO allocateServer(int requestedCapacity)
    {
        ArrayList<ServerDTO> servers = getAllServers();
        for(ServerDTO server : servers)
        {
            if(server.getRemainingCapacity()>=requestedCapacity)
            {
                server.setRemainingCapacity(server.getRemainingCapacity()-requestedCapacity);
                serverRepository.save(modelMapper.map(server,ServerDAO.class));
                return server;
            }
        }
        return null;
    }
    private ServerDTO saveServer(ServerDTO subscribedServer)
    {

        subscribedServer = new ServerDTO(ServerService.currentId,ServerService.CAPACITY,ServerService.CAPACITY,true);
        ServerService.currentId+=1;
        ServerDAO NewRepositoryServer= modelMapper.map(subscribedServer,ServerDAO.class);
        serverRepository.save(NewRepositoryServer);

        return subscribedServer;
    }

    public ServerDTO subscribe(int id , int requestedCapacity)
    {

        ServerDTO subscribedServer =  allocateServer(requestedCapacity);
        if(subscribedServer!= null)
        {
            return saveServer(subscribedServer);
        }

        while (checkForCreatingServers(requestedCapacity));

        subscribedServer = allocateServer(requestedCapacity);
        if(subscribedServer!= null)
        {
            return saveServer(subscribedServer);
        }


        CreateServer createServer = new CreateServer(requestedCapacity,serverRepository,modelMapper);
        Thread newServerThread = new Thread(createServer);
        newServerThread.start();

        return saveServer(subscribedServer);
    }


}
