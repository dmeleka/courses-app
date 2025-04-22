package com.sumerge.task.services;

import com.sumerge.task.dtos.AuthorDTO;
import com.sumerge.task.exceptions.AuthorNotFoundException;
import com.sumerge.task.mappers.AuthorMapper;
import com.sumerge.task.models.Author;
import com.sumerge.task.repositories.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorService authorService;

    private Author author;
    private AuthorDTO authorDTO;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(1L);
        author.setName("Test Author");
        author.setEmail("test@email.com");

        authorDTO = new AuthorDTO();
        authorDTO.setId(1L);
        authorDTO.setName("Test Author");
        authorDTO.setEmail("test@email.com");
    }

    @Test
    public void addAuthor_authorIsSaved_shouldReturnSavedAuthorDTO() {
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.toDTO(author)).thenReturn(authorDTO);

        AuthorDTO output = authorService.addAuthor(author);

        assertNotNull(output);
        assertAll("Attributes",
                () -> assertEquals(authorDTO.getId(), output.getId()),
                () -> assertEquals(authorDTO.getName(), output.getName())
        );
    }

    @Test
    public void getAuthorByEmail_authorFound_shouldReturnAuthorDTO() {

        when(authorRepository.findByEmail(author.getEmail())).thenReturn(Optional.of(author));
        when(authorMapper.toDTO(author)).thenReturn(authorDTO);

        AuthorDTO output = authorService.getAuthorByEmail(author.getEmail());

        assertNotNull(output);
        assertAll("Attributes",
                () -> assertEquals(authorDTO.getId(), output.getId()),
                () -> assertEquals(authorDTO.getName(), output.getName())
        );
    }

    @Test
    public void getAuthorByEmail_authorNotFound_shouldThrowAuthorNotFoundException() {
        when(authorRepository.findByEmail(author.getEmail())).thenReturn(Optional.empty());

        AuthorNotFoundException exception = assertThrows(AuthorNotFoundException.class, () -> {
            authorService.getAuthorByEmail(author.getEmail());
        });
        assertEquals("Author not found with email: " + author.getEmail(), exception.getMessage());
    }
}
