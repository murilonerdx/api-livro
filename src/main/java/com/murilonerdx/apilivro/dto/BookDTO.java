package com.murilonerdx.apilivro.dto;

import com.sun.istack.NotNull;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BookDTO {
  private Long id;
  @NotEmpty
  private String title;
  @NotEmpty
  private String author;
  @NotEmpty
  private String isbn;
}
