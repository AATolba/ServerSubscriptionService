package com.subscription.server.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter

public class WaitingThread extends Thread{
    ServerService serverService ;
    int requestedCapacity;

    public void run(){
        while (serverService.checkForCreatingServers(requestedCapacity))
        {

        }

    }
}
