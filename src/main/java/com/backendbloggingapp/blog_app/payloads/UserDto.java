package com.backendbloggingapp.blog_app.payloads;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
// data issi mein aa raha user se, toh idhar hee validate karenge data ko
public class UserDto implements Serializable {
    //Only use fields we take from user. This data we can directly expose in Api.
    private Integer id;

    @NotEmpty
    @Size(min = 4, message = "Username must be min of 4 characters !!")
    private String name;

    @Email(message = "Email address is not valid !!")
    @NotEmpty(message = "Email is required !!")
    private String email;

    @NotEmpty
    private String about;

    @NotEmpty
    @Size(min = 3, max = 10, message = "Password must be min of 3 chars and max of 10 chars !!")
    private String password;
}
