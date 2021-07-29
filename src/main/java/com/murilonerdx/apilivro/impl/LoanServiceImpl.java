package com.murilonerdx.apilivro.impl;

import com.murilonerdx.apilivro.entity.Loan;
import com.murilonerdx.apilivro.exceptions.BusinessException;
import com.murilonerdx.apilivro.repository.LoanRepository;
import com.murilonerdx.apilivro.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;

public class LoanServiceImpl implements LoanService {

  private final LoanRepository repository;

  @Autowired
  public LoanServiceImpl(LoanRepository repository){
    this.repository = repository;
  }

  @Override
  public Loan save(Loan loan) {
    if(repository.existsByBookAndNotReturn(loan.getBook())){
      throw new BusinessException("Book already loaned");
    }
    return repository.save(loan);
  }
}
