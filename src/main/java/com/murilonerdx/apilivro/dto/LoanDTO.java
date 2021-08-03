package com.murilonerdx.apilivro.dto;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDTO {
  private Long id;
  @NotEmpty
  private String isbn;
  @NotEmpty
  private String customer;
  @NotEmpty
  private BookDTO book;
  private String email;
}
