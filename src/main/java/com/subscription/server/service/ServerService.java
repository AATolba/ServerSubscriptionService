package com.subscription.server.service;
import com.subscription.server.DTO.ServerDTO;
import com.subscription.server.DTO.UserDTO;
import com.subscription.server.Error.Error;
import com.subscription.server.model.ServerDAO;
import com.subscription.server.model.UserDAO;
import com.subscription.server.modelMapper.ModelMapper;
import com.subscription.server.repository.ServerRepository;
import com.subscription.server.repository.UserRepository;
import com.subscription.server.vaildation.Valid;
import jdk.nashorn.internal.runtime.Context;
import lombok.Synchronized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.JtaTransactionAnnotationParser;

import java.awt.image.RescaleOp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.RecursiveTask;


@Service
public class ServerService {

    @Autowired
    ServerRepository serverRepository;
    ModelMapper modelMapper = new ModelMapper();
    public static final int CAPACITY = 100;
    static volatile ArrayList<Integer> runningOperations  = new ArrayList<>();
    public static volatile int creatingServers ;
    public static volatile int currentId ;
    Error error = new Error();
    Valid valid = new Valid();
    Logger logger = LoggerFactory.getLogger(ServerService.class);
    private ArrayList<ServerDTO> getAllServers()
    {
        try
        {
            ArrayList<ServerDAO> serversDAO = (ArrayList<ServerDAO>) serverRepository.findAll();
            if(serversDAO.size()==0){
                return new ArrayList<ServerDTO>();
            }
            else
            {
               ArrayList<ServerDTO> serverDTOArrayList = new ArrayList<>();
               for(ServerDAO server : serversDAO){
                   serverDTOArrayList.add(modelMapper.serverDAO2DTO(server));
               }
               return serverDTOArrayList;
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public int getId(){

        ArrayList<ServerDAO> servers = (ArrayList<ServerDAO>) serverRepository.findAll();
        return servers.size()+1;

    }

    private ServerDTO getServer(int serverId)
    {
        try
        {
            return modelMapper.serverDAO2DTO(Objects.requireNonNull(serverRepository.findById(serverId).orElse(null)));
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
                while (creatingServers == currentCount) {}
                sum = (int) runningOperations.stream().mapToInt(x -> x).summaryStatistics().getSum();
                if (CAPACITY * creatingServers - (requestedCapacity + sum) >= 0)
                {
                    checkForCreatingServers(requestedCapacity);
                }
            }
        }
        return false;
    }
    @Synchronized
    private ServerDTO allocateServer(int requestedCapacity)
    {
        ArrayList<ServerDTO> servers = (ArrayList<ServerDTO>) getAllServers();
        for(ServerDTO server : servers)
        {
            if(server.getRemCapacity()>=requestedCapacity)
            {

                server.setRemCapacity(server.getRemCapacity()-requestedCapacity);
                serverRepository.save(modelMapper.serverDTO2DAO(server));
                return server;
            }
        }
        return null;
    }
    @Synchronized
    private ResponseEntity saveServer(ServerDTO subscribedServer,int capacity)
    {

        currentId = getId();
        if(subscribedServer!=null){
            return new ResponseEntity(subscribedServer,HttpStatus.OK);
        }
        subscribedServer = new ServerDTO(ServerService.currentId,ServerService.CAPACITY-capacity,ServerService.CAPACITY);
        ServerService.currentId++;
        ServerDAO NewRepositoryServer= modelMapper.serverDTO2DAO(subscribedServer);
//        ServerDAO NewRepositoryServer = new ServerDAO(1,100,100);
        try{
        serverRepository.save(NewRepositoryServer);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<>(error.InternalServerError(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(subscribedServer, HttpStatus.OK);
    }

//    @Synchronized
    public ResponseEntity subscribe(int id , int requestedCapacity)  {

        if(requestedCapacity>100){
            return new ResponseEntity(error.ServerCapacityError(),HttpStatus.BAD_REQUEST);
        }
        ServerDTO subscribedServer = allocateServer(requestedCapacity);

        if (subscribedServer != null) {
            return saveServer(subscribedServer,requestedCapacity);
        }
        while (checkForCreatingServers(requestedCapacity)) {
        }

        subscribedServer = allocateServer(requestedCapacity);
        if (subscribedServer != null) {
            return saveServer(subscribedServer, requestedCapacity);
        }

        try
        {
            CreateServer createServer = new CreateServer(requestedCapacity, serverRepository, modelMapper);
            Thread newServerThread = new Thread(createServer);
            newServerThread.start();
            newServerThread.join();
        }
        catch (Exception e )
        {
            logger.error(e.getMessage());
            new ResponseEntity<>(error.InternalServerError(),HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return saveServer(subscribedServer, requestedCapacity);
    }
    public ResponseEntity deleteAll(){
        try {
            serverRepository.deleteAll();
            return new ResponseEntity(valid.deleteSuccess(),HttpStatus.OK);
        }
        catch (Exception e ){
            logger.error(e.getMessage());
            return new ResponseEntity<>(error.InternalServerError(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ArrayList<ServerDAO> getAll(){
        return (ArrayList<ServerDAO>) serverRepository.findAll();
    }

}
