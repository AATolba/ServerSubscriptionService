package com.subscription.server.repository;

import com.subscription.server.model.ServerDAO;
import org.springframework.data.aerospike.repository.AerospikeRepository;

public interface ServerRepository extends AerospikeRepository<ServerDAO,Object> {
}
