package com.backendbloggingapp.blog_app.controllers;

import com.backendbloggingapp.blog_app.exceptions.ApiException;
import com.backendbloggingapp.blog_app.exceptions.ResourceNotFoundException;
import com.backendbloggingapp.blog_app.models.User;
import com.backendbloggingapp.blog_app.payloads.JwtAuthRequest;
import com.backendbloggingapp.blog_app.payloads.JwtAuthResponse;
import com.backendbloggingapp.blog_app.payloads.UserDto;
import com.backendbloggingapp.blog_app.repository.UserRepository;
import com.backendbloggingapp.blog_app.security.JwtTokenHelper;
import com.backendbloggingapp.blog_app.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;//jiss method ki bean security config mein banayi hai
    //ye authentication manager humara authentication kar sakta hai

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) throws Exception {

        this.authenticate(request.getUsername(), request.getPassword());
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
        String token = this.jwtTokenHelper.generateToken(userDetails);
        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(token);
        User user = this.userRepository.findByEmail(request.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User", request.getUsername() +  " Not found ", 0));
        UserDto userDto = this.modelMapper.map(user,UserDto.class);
        response.setUser(userDto);

        return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);

    }

    private void authenticate(String username, String password) throws Exception {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);

        try {

            this.authenticationManager.authenticate(authenticationToken);

        } catch (BadCredentialsException e) {
            System.out.println("Invalid Detials !!");
            throw new ApiException("Invalid username or password !!");
        }

    }

    // register new user api

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {
        UserDto registeredUser = this.userService.registerNewUser(userDto);
        return new ResponseEntity<UserDto>(registeredUser, HttpStatus.CREATED);
    }

}
