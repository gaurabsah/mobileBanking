package com.bank.mobileBanking.controller;

import com.bank.mobileBanking.dto.UserDTO;
import com.bank.mobileBanking.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String,Object> register(@RequestBody UserDTO userDTO){
        log.info("inside register()::controller");
        return userService.register(userDTO);
    }

}
