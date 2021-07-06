package com.murilonerdx.apilivro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookDTO {
  private Long id;
  private String title;
  private String author;
  private String isbn;
}
