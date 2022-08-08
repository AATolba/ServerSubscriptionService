package com.subscription.server.service;
import com.subscription.server.DTO.ServerDTO;
import com.subscription.server.DTO.UserDTO;
import com.subscription.server.Error.Error;
import com.subscription.server.model.ServerDAO;
import com.subscription.server.modelMapper.ModelMapper;
import com.subscription.server.repository.ServerRepository;
import com.subscription.server.repository.UserRepository;
import com.subscription.server.vaildation.ValidMessage;
import lombok.Synchronized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service
public class ServerService {

    @Autowired
    ServerRepository serverRepository;
    @Autowired
    UserRepository userRepository;
    ModelMapper modelMapper = new ModelMapper();
    boolean finished = false;
    public static final int CAPACITY = 100;
    static volatile ArrayList<Integer> runningOperations  = new ArrayList<>();
    public static volatile int creatingServers ;
    public static volatile int currentId ;
    Error error = new Error();
    ValidMessage valid = new ValidMessage();
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

    public boolean checkForCreatingServers(int requestedCapacity)
    {
        if(runningOperations.size()>0)
        {
            int sum = (int) runningOperations.stream().mapToInt(x -> x).summaryStatistics().getSum();
            if (CAPACITY * creatingServers - (requestedCapacity + sum) >= 0)
            {
                int currentCount = creatingServers;
                while (creatingServers == currentCount) {

                }
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
    public ServerDTO allocateServer(int requestedCapacity)
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
    public ResponseEntity saveServer(@Nullable  ServerDTO subscribedServer, int capacity)
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




    public synchronized ResponseEntity subscribe(int id , int requestedCapacity)  {

        if(requestedCapacity>100){
            return new ResponseEntity(error.ServerCapacityError(), HttpStatus.BAD_REQUEST);
        }

        if(userRepository.findById(id).orElse(null)==null){
            return new ResponseEntity(error.UserDoesNOtExist(),HttpStatus.NOT_FOUND);
        }
        ServerDTO subscribedServer = allocateServer(requestedCapacity);

        if (subscribedServer != null) {
            return saveServer(subscribedServer, requestedCapacity);
        }


        WaitingThread thread = new WaitingThread(this,requestedCapacity);
        thread.start();
//        while (checkForCreatingServers(requestedCapacity))
//        {
//        }
        subscribedServer = allocateServer(requestedCapacity);
        if (subscribedServer != null) {
            saveServer(subscribedServer, requestedCapacity);
        }

        try
        {
            CreateServer createServer = new CreateServer(requestedCapacity, serverRepository, modelMapper,this , finished);
            Thread newServerThread = new Thread(createServer);
            newServerThread.start();
        }
        catch (Exception e )
        {
            logger.error(e.getMessage());
            new ResponseEntity<>(error.InternalServerError(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

//        saveServer(null,requestedCapacity);
        return new ResponseEntity(error.WaitingForServer(),HttpStatus.OK);
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

    public List<ServerDAO> getAll(){
        List<ServerDAO> list = (ArrayList<ServerDAO>) serverRepository.findAll();
         list.sort(ServerDAO::compareTo);
         return list;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
