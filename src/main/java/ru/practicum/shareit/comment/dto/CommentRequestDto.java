package ru.practicum.shareit.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.additionally.Create;
import ru.practicum.shareit.additionally.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
public class CommentRequestDto {
    @NotBlank(groups = {Create.class})
    @Size(max = 300, groups = {Create.class, Update.class})
    private String text;
}
