package com.SpringBootProject.ToDo.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "todo_user")
@Getter
@Setter
public class User {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    @NonNull
    private String userName;

    @NonNull
    private String userPin;

    private String email;

    @DBRef
    private List<ToDoEntity> toDoList = new ArrayList<>();

    private List<String> roles;

    public ObjectId getId() {
        return id;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public @NonNull String getUserName() {
        return userName;
    }

    public void setUserName(@NonNull String userName) {
        this.userName = userName;
    }

    public @NonNull String getUserPin() {
        return userPin;
    }

    public void setUserPin(@NonNull String userPin) {
        this.userPin = userPin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ToDoEntity> getToDoList() {
        return toDoList;
    }

    public void setToDoList(List<ToDoEntity> toDoList) {
        this.toDoList = toDoList;
    }
}