package com.backendbloggingapp.blog_app.service.Impl;

import com.backendbloggingapp.blog_app.config.AppConstants;
import com.backendbloggingapp.blog_app.exceptions.ResourceNotFoundException;
import com.backendbloggingapp.blog_app.models.Role;
import com.backendbloggingapp.blog_app.models.User;
import com.backendbloggingapp.blog_app.payloads.UserDto;
import com.backendbloggingapp.blog_app.repository.RoleRepository;
import com.backendbloggingapp.blog_app.repository.UserRepository;
import com.backendbloggingapp.blog_app.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepo;

    @Override
    public UserDto registerNewUser(UserDto userDto) {

        User user = this.modelMapper.map(userDto, User.class);

        // encoded the password
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        // roles
        Role role = this.roleRepo.findById(AppConstants.NORMAL_USER).get();

        user.getRoles().add(role);

        User newUser = this.userRepository.save(user);

        return this.modelMapper.map(newUser, UserDto.class);
    }
    @Override
    public UserDto createUser(UserDto userDto) {
        //userRepository.save(userDto);//but problem, we have userDto, convert it to User
        User user = dtoToUser(userDto);
        User savedUser = userRepository.save(user);
        return userToDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
        user.setAbout(userDto.getAbout());

        User updatedUser = this.userRepository.save(user);
        UserDto userDto1 = this.userToDto(updatedUser);
        return userDto1;
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));

        return this.userToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = this.userRepository.findAll();
        List<UserDto> userDtos = users.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());

        return userDtos;
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        this.userRepository.delete(user);

    }
    //Till now, all CRUD operations are done

    // make 2 functions to convert user to userDto, vice versa
    public User dtoToUser(UserDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);//ye conversion apne aap kardega
        // map.(jisse convert karna hai,jiss class mein)
        //User user = new User();

//         user.setId(userDto.getId());
//         user.setName(userDto.getName());
//         user.setEmail(userDto.getEmail());
//         user.setAbout(userDto.getAbout());
//         user.setPassword(userDto.getPassword());
        return user;
    }

    public UserDto userToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setAbout(user.getAbout());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());

        return userDto;
    }


}
