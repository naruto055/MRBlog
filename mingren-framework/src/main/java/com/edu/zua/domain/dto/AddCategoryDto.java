package com.edu.zua.domain.dto;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCategoryDto {
    private String description;
    private Long id;
    private String name;
    private String status;
}
