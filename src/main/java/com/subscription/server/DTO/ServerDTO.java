package com.subscription.server.DTO;

import lombok.*;
import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Document
@Data
public class ServerDTO {
    @Id
    int id;
    int remainingCapacity;
    int fullCapacity;
    boolean status;
}

