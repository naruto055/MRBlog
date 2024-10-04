package com.edu.zua.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkDto {
    private Integer pageNum;
    private Integer pageSize;
    private String name;
    private String status;
}
