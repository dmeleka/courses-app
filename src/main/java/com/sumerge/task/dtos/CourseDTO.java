package com.sumerge.task.dtos;

import com.sumerge.task.models.Lang;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {

    private long id;
    private String name;
    private int credit;
    private Lang lang;
}

