package com.unibuc.auclicenta.controller.user;

import com.unibuc.auclicenta.controller.listing.TopUpRequest;
import com.unibuc.auclicenta.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("{id}")
    @ResponseBody
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("{id}")
    @ResponseBody
    public ResponseEntity<String> changePassword(@PathVariable("id") String id, @RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(userService.changePassword(id, request));
    }

    @PutMapping("balance/{id}")
    public ResponseEntity<String> topUpUser(@PathVariable("id") String id, @RequestBody TopUpRequest request){
        return ResponseEntity.ok(userService.topUp(request.getBalance(), id));
    }
    //TODO update user email/name
    @DeleteMapping("{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable("id") String id){
        return ResponseEntity.ok(userService.deleteUser(id));
    }
}
