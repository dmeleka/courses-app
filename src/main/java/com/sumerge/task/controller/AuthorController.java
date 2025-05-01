package com.sumerge.task.controller;

import com.sumerge.task.dto.AuthorDTO;
import com.sumerge.task.model.Author;
import com.sumerge.task.service.AuthorService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/authors")
@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author added"),
            @ApiResponse(responseCode = "401", description = "Unauthenticated Author or invalid/missing x-validation-report header")
    })
    @PostMapping("/")
    public ResponseEntity<AuthorDTO> addAuthor(@RequestBody Author author) {
        return ResponseEntity.ok(authorService.addAuthor(author));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author returned"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
            @ApiResponse(responseCode = "401", description = "invalid/missing x-validation-report header")
    })
    @GetMapping("/")
    public ResponseEntity<AuthorDTO> getAuthorByEmail(@RequestParam String email) {
        return ResponseEntity.ok(authorService.getAuthorByEmail(email));
    }
}