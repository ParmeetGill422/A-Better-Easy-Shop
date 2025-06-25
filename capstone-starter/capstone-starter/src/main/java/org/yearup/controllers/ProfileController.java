package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

@RestController
@RequestMapping("/profile")
@CrossOrigin
public class ProfileController {

    private final UserDao userDao;
    private final ProfileDao profileDao;

    @Autowired
    public PrfileController(UserDao userDao, ProfileDao profileDao){
        this.userDao = userDao;
        this.profileDao = profileDao;
    }

    @GetMapping
    public Profile getProfile(Authentication authentication){
        String username = authentication.getName();
        User user = userDao.getByUserName(username);

        if (user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        Profile profile = profileDao.getByUserId(user.get)
    }
}
