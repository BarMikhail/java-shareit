package ru.practicum.shareit.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.additionally.Create;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CommentDto {
    private Long id;
    @NotBlank(groups = {Create.class})
    private String text;
    private String authorName;
    private LocalDateTime created = LocalDateTime.now();
}