package com.subscription.server.model;

import lombok.*;
import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Document
@Data
public class UserDAO {
    @Id
    int id ;
    String name;

    String email;
}
