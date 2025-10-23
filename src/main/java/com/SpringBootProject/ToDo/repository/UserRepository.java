package com.SpringBootProject.ToDo.repository;

import com.SpringBootProject.ToDo.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository
        extends MongoRepository<User, ObjectId> {
    User findByUserName(String userName);

    void deleteByUserName(String userName);
}
