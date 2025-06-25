package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ProfileController(UserDao userDao, ProfileDao profileDao) {
        this.userDao = userDao;
        this.profileDao = profileDao;
    }


    @GetMapping
    public Profile getProfile(Authentication authentication) {
        String username = authentication.getName();
        User user = userDao.getByUserName(username);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Profile profile = profileDao.getByUserId(user.getUserId());

        if (profile == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found");
        }

        return profile;
    }


    @PutMapping
    public void updateProfile(@RequestBody Profile profile, Authentication authentication) {
        String username = authentication.getName();
        User user = userDao.getByUserName(username);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        profileDao.update(user.getUserId(), profile);
    }
}
