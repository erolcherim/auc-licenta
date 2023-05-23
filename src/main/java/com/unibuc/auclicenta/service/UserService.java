package com.unibuc.auclicenta.service;

import com.unibuc.auclicenta.controller.user.ChangePasswordRequest;
import com.unibuc.auclicenta.controller.user.UserResponse;
import com.unibuc.auclicenta.data.user.User;
import com.unibuc.auclicenta.exception.PasswordsDoNotMatchException;
import com.unibuc.auclicenta.exception.SamePasswordException;
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

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException();
        } else if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new SamePasswordException();
        }

        user.setPassword(encodedPassword);
        userRepository.save(user);

        //TODO force logout of user when password is changed (might be done by calling logout() in frontend)
        return "Password updated successfully";
    }

    public String getUserIdByEmail(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        return user.getId();
    }
}
