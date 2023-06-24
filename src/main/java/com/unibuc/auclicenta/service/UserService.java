package com.unibuc.auclicenta.service;

import com.unibuc.auclicenta.controller.user.ChangePasswordRequest;
import com.unibuc.auclicenta.controller.user.UserResponse;
import com.unibuc.auclicenta.data.user.User;
import com.unibuc.auclicenta.exception.*;
import com.unibuc.auclicenta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
                .balance(user.getBalance())
                .email(user.getEmail())
                .build();
    }

    public UserResponse getCurrentUser() {
        String userId = getUserIdByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .balance(user.getBalance())
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

        return "Password updated successfully";
    }

    public Boolean userExists(String id) {
        return userRepository.findById(id).isPresent();
    }

    public String getUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        return user.getId();
    }

    public UserResponse deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(id);
        return UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .balance(user.getBalance())
                .build();
    }

    public int modifyBalance(int balance, String id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        int currentBalance = user.getBalance();
        if (currentBalance - balance >= 0) {
            user.setBalance(currentBalance - balance);
            userRepository.save(user);
            return user.getBalance();
        } else {
            throw new InsufficientFundsException();
        }
    }

    public String topUp(int amount, String id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        int currentBalance = user.getBalance();
        if (amount >= 5) {
            user.setBalance(currentBalance + amount);
            userRepository.save(user);
            return "Funds operation completed successfully";
        } else {
            throw new InvalidTopUpAmountException();
        }
    }

    public void refundBid(int amount, String id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        int currentBalance = user.getBalance();
        user.setBalance(currentBalance + amount);
        userRepository.save(user);
    }
}
