package com.murilonerdx.apilivro.controller;

import com.murilonerdx.apilivro.dto.BookDTO;
import com.murilonerdx.apilivro.entity.Book;
import com.murilonerdx.apilivro.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

  private final BookService service;
  private ModelMapper modelMapper;

  @Autowired
  public BookController(BookService service, ModelMapper mapper) {
    this.modelMapper = mapper;
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BookDTO create(@RequestBody BookDTO dto) {
//    Book entity = Book.builder().author(dto.getAuthor()).title(dto.getTitle()).isbn(dto.getIsbn()).build();
    Book entity = modelMapper.map(dto,Book.class);
    entity = service.save(entity);
//    return BookDTO.builder().id(entity.getId()).author("Murilo").title("Meu livro").isbn("1234").build();
  return modelMapper.map(entity, BookDTO.class);
  }
}
