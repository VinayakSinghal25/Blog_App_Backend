package com.backendbloggingapp.blog_app.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    @Column(name = "post_title", length = 100, nullable = false)
    private String title;

    @Column(length = 1000000000)
    private String content;

    private String imageName;

    private Date addedDate;

    @ManyToOne
    @JoinColumn(name = "category_id")// join column ki foreign key jo hogi Post table mein
    // ,uska name change karne ke liye
    private Category category;

    @ManyToOne
    private User user;

    // aur agar post mil gaya, toh woh kis category ka hai, and kis user ne kiya hai, dekh sakte hain

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private Set<Comment> comments=new HashSet<>();

}
