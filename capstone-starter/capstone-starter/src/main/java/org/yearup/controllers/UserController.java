package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.UserDao;
import org.yearup.data.ProfileDao;
import org.yearup.models.User;
import org.yearup.models.Profile;

@RestController
public class UserController {

    private final UserDao userDao;
    private final ProfileDao profileDao;

    @Autowired
    public UserController(UserDao userDao, ProfileDao profileDao) {
        this.userDao = userDao;
        this.profileDao = profileDao;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody User user) {
        // Check if username already exists
        if (userDao.exists(user.getUsername())) {
            throw new RuntimeException("Username already taken.");
        }

        // Create the user in DB
        User createdUser = userDao.create(user);
        if (createdUser == null) {
            throw new RuntimeException("User registration failed.");
        }

        // Create a default profile linked to the new user
        Profile profile = new Profile();
        profile.setUserId(createdUser.getId());
        profileDao.create(profile);

        // Clear the password before returning the user object
        createdUser.setPassword(null);
        return createdUser;
    }
}
