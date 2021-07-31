package com.murilonerdx.apilivro.service;

import com.murilonerdx.apilivro.entity.Loan;
import java.util.Optional;

public interface LoanService {
    Loan save(Loan loan);
    Optional<Loan> getById(Long id);
    void update(Loan loan);
}
