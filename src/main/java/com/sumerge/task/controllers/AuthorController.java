package com.sumerge.task.controllers;

import com.sumerge.task.dtos.AuthorDTO;
import com.sumerge.task.mappers.AuthorMapper;
import com.sumerge.task.models.Author;
import com.sumerge.task.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/authors")
@RestController
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;

    @Autowired
    public AuthorController(AuthorService authorService, AuthorMapper authorMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }

    @PostMapping("/addAuthor")
    public ResponseEntity<AuthorDTO> addAuthor(@RequestBody Author author) {
        return ResponseEntity.ok(authorService.addAuthor(author));
    }

    @GetMapping("/getAuthorByEmail")
    public ResponseEntity<AuthorDTO> getAuthorByEmail(@RequestBody AuthorDTO authorDTO) {
        return ResponseEntity.ok(authorService.getAuthorByEmail(authorMapper.toEntity(authorDTO).getEmail()));
    }
}
