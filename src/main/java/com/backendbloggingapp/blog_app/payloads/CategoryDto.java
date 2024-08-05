package com.backendbloggingapp.blog_app.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto implements Serializable {

    private Integer categoryId;
    @NotBlank
    @Size(min = 4,message = "Min size of category title is 4")
    private String categoryTitle;

    @NotBlank
    @Size(min = 10, message = "min size of cateogry desc is 10")
    private String categoryDescription;

}