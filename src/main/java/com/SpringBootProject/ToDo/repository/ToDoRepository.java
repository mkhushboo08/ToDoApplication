package com.SpringBootProject.ToDo.repository;

import com.SpringBootProject.ToDo.entity.ToDoEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ToDoRepository
        extends MongoRepository<ToDoEntity, ObjectId> {

}