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
public class UserDTO {
    @Id
    int id ;
    String name;
    String email;

}

