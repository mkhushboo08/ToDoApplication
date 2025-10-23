package com.SpringBootProject.ToDo.controller;

import com.SpringBootProject.ToDo.entity.ToDoEntity;
import com.SpringBootProject.ToDo.entity.User;
import com.SpringBootProject.ToDo.service.ToDoService;
import com.SpringBootProject.ToDo.service.UserService;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@RestController
@RequestMapping("/todo")
public class ToDoController {

    @Autowired
    private ToDoService toDoService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ToDoEntity> createToDo(
            @RequestBody ToDoEntity toDo) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            toDoService.saveEntry(userName, toDo);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //see all todo entries
    @GetMapping
    public ResponseEntity<List<ToDoEntity>> getToDo() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User user = userService.findByUserName(userName);
        List<ToDoEntity> all = user.getToDoList();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // see todo particular entry by id
    @GetMapping("/id/{myId}")
    public ResponseEntity<?> getToDoById(
            @PathVariable ObjectId myId
    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User user1 = userService.findByUserName(userName);
        List<ToDoEntity> collect = user1.getToDoList().stream()
                .filter(x -> x.getId().equals(myId))
                .collect(Collectors.toList());

        if (!collect.isEmpty()){
            Optional<ToDoEntity> todoEntry = toDoService.findById(myId);
            if (todoEntry.isPresent()) {
                return new ResponseEntity<>(todoEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
    //delete all todo
    @DeleteMapping("/delete-all-todo")
    public ResponseEntity<?> deleteALLToDo() {
        toDoService.deleteALlToDo();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
     */

    //delete todo: user -> particular entry by id
    @DeleteMapping("/id/{myId}")
    public ResponseEntity<?> deleteToDo(
            @PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean deleted = toDoService.deleteToDoById(userName, myId);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //update todo: user -> particular entry by id
    @PutMapping("/id/{myId}")
    public ResponseEntity<ToDoEntity> updateToDo(
            @PathVariable ObjectId myId,
            @RequestBody ToDoEntity newBody) {

        //we have authenticated the user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        //find the user from db
        User user = userService.findByUserName(userName);

        //find the todo entry
        List<ToDoEntity> collect = user.getToDoList().stream()
                .filter(x -> x.getId().equals(myId))
                .collect(Collectors.toList());
        if (!collect.isEmpty()) {
            Optional<ToDoEntity> todoEntry = toDoService.findById(myId); //user->todo entry by ID
            if (todoEntry.isPresent()){
                ToDoEntity oldBody = todoEntry.get();
                String title = newBody.getTitle();
                if (title != null && !title.isEmpty()) {
                    oldBody.setTitle(title);
                }
                String description = newBody.getDescription();
                if (description != null && !description.isEmpty()) {
                    oldBody.setDescription(description);
                }
                toDoService.saveEntry(oldBody);
                return new ResponseEntity<>(oldBody, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
