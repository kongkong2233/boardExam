package org.example.boardexam.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Board {
    @Id
    private Long id;
    private String name;
    private String title;
    private String password;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
