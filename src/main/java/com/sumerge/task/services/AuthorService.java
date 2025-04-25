package com.sumerge.task.services;

import com.sumerge.task.dtos.AuthorDTO;
import com.sumerge.task.exceptions.AuthorNotFoundException;
import com.sumerge.task.exceptions.EmailAlreadyExistsException;
import com.sumerge.task.mappers.AuthorMapper;
import com.sumerge.task.models.Author;
import com.sumerge.task.repositories.AuthorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper, PasswordEncoder passwordEncoder) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthorDTO addAuthor(@RequestBody Author author) {
        if (authorRepository.existsByEmail(author.getEmail())) {
            throw new EmailAlreadyExistsException(author.getEmail());
        }
        author.setPassword(passwordEncoder.encode(author.getPassword()));
        return authorMapper.toDTO(authorRepository.save(author));
    }

    public AuthorDTO getAuthorByEmail(String email) {
        Author author = authorRepository.findByEmail(email).orElseThrow(() -> new AuthorNotFoundException("Author not found with email: " + email));
        return (authorMapper.toDTO(author));
    }
}