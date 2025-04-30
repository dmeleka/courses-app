package com.sumerge.task.controller;

import com.sumerge.task.dto.AuthorDTO;
import com.sumerge.task.model.Author;
import com.sumerge.task.service.AuthorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/authors")
@RestController
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/add")
    public ResponseEntity<AuthorDTO> addAuthor(@RequestBody Author author) {
        return ResponseEntity.ok(authorService.addAuthor(author));
    }

    @GetMapping("/getByEmail")
    public ResponseEntity<AuthorDTO> getAuthorByEmail(@RequestParam String email) {
        return ResponseEntity.ok(authorService.getAuthorByEmail(email));
    }
}
