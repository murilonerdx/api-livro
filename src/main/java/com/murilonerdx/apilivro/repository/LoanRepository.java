package com.murilonerdx.apilivro.repository;

import com.murilonerdx.apilivro.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {

}
