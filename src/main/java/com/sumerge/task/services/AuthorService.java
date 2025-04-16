package com.sumerge.task.services;

import com.sumerge.task.dtos.AuthorDTO;
import com.sumerge.task.mappers.AuthorMapper;
import com.sumerge.task.models.Author;
import com.sumerge.task.repositories.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    public AuthorDTO addAuthor(@RequestBody Author author) {
        return authorMapper.toDTO(authorRepository.save(author));
    }

    public AuthorDTO getAuthorByEmail(String email) {
        return (authorMapper.toDTO(authorRepository.findByEmail(email)));
    }
}