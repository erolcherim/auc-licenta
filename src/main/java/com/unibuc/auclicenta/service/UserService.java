package com.unibuc.auclicenta.service;

import com.unibuc.auclicenta.controller.user.ChangePasswordRequest;
import com.unibuc.auclicenta.controller.user.UserResponse;
import com.unibuc.auclicenta.data.users.User;
import com.unibuc.auclicenta.exception.PasswordsDoNotMatchException;
import com.unibuc.auclicenta.exception.UserNotFoundException;
import com.unibuc.auclicenta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        return UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    public String changePassword(String id, ChangePasswordRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException();
        }
        // TODO store old passwords in db to check if newpass == oldpass
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        //TODO force logout of user when password is changed
        return "Password updated successfully";
    }

}