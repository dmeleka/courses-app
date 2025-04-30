package com.sumerge.task.mapper;

import com.sumerge.task.dto.CourseDTO;
import com.sumerge.task.model.Course;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseDTO toDTO(Course course);

    @Mapping(target = "id", ignore = true)
    void updateCourseFromDTO(CourseDTO dto, @MappingTarget Course entity);
}
