package com.murilonerdx.apilivro.repository;

import com.murilonerdx.apilivro.entity.Book;
import com.murilonerdx.apilivro.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

  @Query(value="select case when (count(l.id) > 0) then true else false end "
  + " from Loan l where l.book = :book and (l.returned is null or l.returned is not true) ")
  boolean existsByBookAndNotReturn(@Param("book") Book book);
}
