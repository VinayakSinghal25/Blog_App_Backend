package com.backendbloggingapp.blog_app.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class PostDto  implements Serializable {

    private Integer postId;

    private String title;

    private String content;
    // only string and content we take from user, but we have rest of the things as well, because in response we
    //send postDto, not post, so for the user to see, needs all things
    private String imageName;

    private Date addedDate;

    private CategoryDto category;//Similarly here

    private UserDto user;//Idhar UserDto liya,because uske andar koi posts nahi hain,
    // isliye infinite recursion nahi hoga

    private Set<CommentDto> comments=new HashSet<>();//so now,jab post ko fetch karenge,
    // toh uske comments apne aap se aa jaayenge
}
