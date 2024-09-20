package com.resul.authenticationservice.service;

import com.resul.authenticationservice.dto.AuthenticationResponseDTO;
import com.resul.authenticationservice.dto.RegisterRequestDTO;
import com.resul.authenticationservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", url = "http://localhost:8090/api/v1/users")
public interface UserServiceClient {

    @GetMapping("/by-username/{username}")
    UserDto getUserByUsername(@PathVariable("username") String username);

    @PostMapping()
    AuthenticationResponseDTO createUser(@RequestHeader("X-Secret-Token") String token, @RequestBody RegisterRequestDTO registerRequestDTO);
}