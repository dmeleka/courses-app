package com.sumerge.task.mappers;

import com.sumerge.task.dtos.AuthorDTO;
import com.sumerge.task.models.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
//    Author toEntity(AuthorDTO authorDTO);

    AuthorDTO toDTO(Author author);
}
