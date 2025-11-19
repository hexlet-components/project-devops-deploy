// Обновление поста
package io.hexlet.spring.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostUpdateDTO {
    private String name;
    private String body;
}
