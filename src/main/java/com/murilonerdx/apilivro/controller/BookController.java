package com.murilonerdx.apilivro.controller;

import com.murilonerdx.apilivro.dto.BookDTO;
import java.awt.print.Book;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

  @PostMapping
  public BookDTO create() {
    BookDTO dto = new BookDTO();
    dto.setAuthor("Murilo");
    dto.setId(1L);
    dto.setTitle("Meu livro");
    dto.setIsbn("123456");
    return dto;
  }
}
