package com.sumerge.task.dto;

import com.sumerge.task.model.Lang;
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

