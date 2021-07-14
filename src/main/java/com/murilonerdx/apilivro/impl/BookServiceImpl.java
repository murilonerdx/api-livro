package com.murilonerdx.apilivro.impl;

import com.murilonerdx.apilivro.entity.Book;
import com.murilonerdx.apilivro.exceptions.BusinessException;
import com.murilonerdx.apilivro.repository.BookRepository;
import com.murilonerdx.apilivro.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * BookServiceImpl
 *
 * @blame Adicionando Bookservice
 */
@Service
public class BookServiceImpl implements BookService {

  private BookRepository repository;

  @Autowired
  public BookServiceImpl(BookRepository repository) {
    this.repository = repository;
  }


  @Override
  public Book save(Book entity) {
    if(repository.existsByIsbn(entity.getIsbn())){
      throw new BusinessException("Isbn já cadastrado.");
    }
    return repository.save(entity);
  }
}
