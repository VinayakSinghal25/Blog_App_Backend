package com.backendbloggingapp.blog_app.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PostResponse  implements Serializable {


    private List<PostDto> content;//content of that page according to page no. and page size will come here
    //content toh list of PostDtos hee hai,jo service mein nikalenge find all mein pageable deke
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean lastPage;

    // all this we will return when someone accesses posts
    // because we have implemented pagination last page and all also we'll send
}