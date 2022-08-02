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
@Builder
public class ServerDTO {
    @Id
    int id;
    int remCapacity;
    int fullCapacity;

}

