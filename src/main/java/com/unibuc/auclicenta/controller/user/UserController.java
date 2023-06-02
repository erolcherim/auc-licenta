package com.unibuc.auclicenta.controller.user;

import com.unibuc.auclicenta.controller.StringResponse;
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

    @GetMapping("")
    public ResponseEntity<UserResponse> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }


    @PutMapping(value = "{id}")
    @ResponseBody
    public ResponseEntity<StringResponse> changePassword(@PathVariable("id") String id, @RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(new StringResponse(userService.changePassword(id, request)));
    }

    @PostMapping("balance/{id}")
    public ResponseEntity<StringResponse> topUpUser(@PathVariable("id") String id, @RequestBody TopUpRequest request){
        return ResponseEntity.ok(new StringResponse(userService.topUp(request.getBalance(), id)));
    }
    //TODO update user email/name
    //TODO add images to listing
    //TODO add auto feature
    @DeleteMapping("{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable("id") String id){
        return ResponseEntity.ok(userService.deleteUser(id));
    }
}
