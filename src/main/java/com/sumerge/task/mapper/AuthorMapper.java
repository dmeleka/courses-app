package com.sumerge.task.mapper;

import com.sumerge.task.dto.AuthorDTO;
import com.sumerge.task.model.Author;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorDTO toDTO(Author author);
}
