package com.sumerge.task.controllers;

import com.sumerge.task.dtos.AuthorDTO;
import com.sumerge.task.models.Author;
import com.sumerge.task.services.AuthorService;
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
