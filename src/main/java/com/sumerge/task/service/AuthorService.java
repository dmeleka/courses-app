package com.sumerge.task.service;

import com.sumerge.task.dto.AuthorDTO;
import com.sumerge.task.exception.AuthorNotFoundException;
import com.sumerge.task.exception.EmailAlreadyExistsException;
import com.sumerge.task.mapper.AuthorMapper;
import com.sumerge.task.model.Author;
import com.sumerge.task.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final PasswordEncoder passwordEncoder;

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