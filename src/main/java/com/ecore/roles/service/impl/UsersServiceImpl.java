package com.ecore.roles.service.impl;

import com.ecore.roles.client.UsersClient;
import com.ecore.roles.client.model.User;
import com.ecore.roles.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersClient usersClient;

    @Autowired
    public UsersServiceImpl(UsersClient usersClient) {
        this.usersClient = usersClient;
    }

    @Override
    public User getUserById(UUID userId) {
        return usersClient.getUser(userId).getBody();
    }

    public List<User> getUsers() {
        return usersClient.getUsers().getBody();
    }
}
