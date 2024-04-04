package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.additionally.Create;

//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Size;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemPostDto {
//    @NotBlank(groups = {Create.class})
//    @Size(max = 200, groups = {Create.class})
    private String description;
}
