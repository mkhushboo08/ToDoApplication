package com.SpringBootProject.ToDo.controller;

import com.SpringBootProject.ToDo.entity.User;
import com.SpringBootProject.ToDo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;


    @PostMapping("/create-admin-user")
    public void createNewAdmin(@RequestBody User user){
        userService.saveAdmin(user);
    }

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUser() {
        List<User> all = userService.getAll();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete-all-user")
    public ResponseEntity<Void> deleteAllUser() {
        userService.deleteAllUser();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
