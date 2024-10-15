package com.bank.mobileBanking.controller;

import com.bank.mobileBanking.dto.AccountDTO;
import com.bank.mobileBanking.dto.LoginDTO;
import com.bank.mobileBanking.dto.UserDTO;
import com.bank.mobileBanking.entity.enums.LoginType;
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

    @PostMapping("/openAccount")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String,Object> openAccount(@RequestParam String email,@RequestBody AccountDTO accountDTO){
        log.info("inside openAccount()::controller");
        return userService.openAccount(email,accountDTO);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Object> login(@RequestBody LoginDTO loginDTO){
        return userService.login(loginDTO);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String,Object> register(@RequestBody UserDTO userDTO){
        log.info("inside register()::controller");
        return userService.register(userDTO);
    }

}
