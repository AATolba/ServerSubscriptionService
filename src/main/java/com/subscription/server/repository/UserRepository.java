package com.subscription.server.repository;


import com.subscription.server.model.UserDAO;
import org.springframework.data.aerospike.repository.AerospikeRepository;

public interface UserRepository extends AerospikeRepository<UserDAO, Object> {
}