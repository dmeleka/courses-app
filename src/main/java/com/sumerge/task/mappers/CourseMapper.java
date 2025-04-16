package com.sumerge.task.mappers;

import com.sumerge.task.dtos.CourseDTO;
import com.sumerge.task.models.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    Course toEntity(CourseDTO courseDTO);

    CourseDTO toDTO(Course course);

    @Mapping(target = "id", ignore = true)
    void updateCourseFromDTO(CourseDTO dto, @MappingTarget Course entity);
}
