package com.subscription.server.model;

import com.subscription.server.service.ServerService;
import lombok.*;
import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Document
@Data
@Builder
public class ServerDAO implements Comparable<ServerDAO>{
    @Id
    int id;
    int remCapacity;
    int fullCapacity;

    @Override
    public int compareTo(ServerDAO serverDAO) {
        if(this.getId()>serverDAO.id){
            return 1;
        }
        if(this.getId()<serverDAO.getId()){
            return -1;
        }
        return 0;
    }
}