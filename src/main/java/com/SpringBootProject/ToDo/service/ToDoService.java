package com.SpringBootProject.ToDo.service;

import com.SpringBootProject.ToDo.entity.ToDoEntity;
import com.SpringBootProject.ToDo.entity.User;
import com.SpringBootProject.ToDo.repository.ToDoRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ToDoService {

    @Autowired
    public ToDoRepository toDoRepository;

    @Autowired
    public UserService userService;

    @Transactional
    public void saveEntry(String userName,
                          ToDoEntity todo){
        try {
            User user = userService.findByUserName(userName);
            todo.setDateCreated(LocalDateTime.now());
            ToDoEntity saved = toDoRepository.save(todo);
            user.getToDoList().add(saved);
            userService.saveUser(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveEntry(ToDoEntity todo){
        toDoRepository.save(todo);
    }

    public List<ToDoEntity> getAll(){
        return toDoRepository.findAll();
    }

    public Optional<ToDoEntity> getToDoById(ObjectId myId){
        return toDoRepository.findById(myId);
    }

    public void deleteALlToDo(){
        toDoRepository.deleteAll();
    }

    @Transactional
    public boolean deleteToDoById(String userName, ObjectId myId){
        boolean removed = false;
        try {
            User user = userService.findByUserName(userName);
            removed = user.getToDoList().removeIf(x -> x.getId().equals(myId));
            if (removed) {
                userService.saveUser(user);
                toDoRepository.deleteById(myId);
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while deleting your todo entry",e);
        }
        return removed;
    }

    public Optional<ToDoEntity> findById(ObjectId id){
        return toDoRepository.findById(id);
    }

}
