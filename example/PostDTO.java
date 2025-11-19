// Вывод поста
package io.hexlet.spring.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostDTO {
    private Long id;
    private String slug;
    private String name;
    private String body;
    private LocalDate createdAt;
}
