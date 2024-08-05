package com.backendbloggingapp.blog_app.payloads;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class CommentDto  implements Serializable {

    private int id;

    private String content;

}