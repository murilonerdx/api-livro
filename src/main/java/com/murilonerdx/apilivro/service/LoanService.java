package com.murilonerdx.apilivro.service;

import com.murilonerdx.apilivro.dto.LoanFilterDTO;
import com.murilonerdx.apilivro.entity.Book;
import com.murilonerdx.apilivro.entity.Loan;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoanService {
    Loan save(Loan loan);
    Optional<Loan> getById(Long id);
    Loan update(Loan loan);
    Page<Loan> find(LoanFilterDTO any, Pageable any1);
    Page<Loan> getLoansByBook(Book book, Pageable any1);
    List<Loan> getAllLateLoans();
}
