//package ru.practicum.shareit.trash;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import ru.practicum.shareit.additionally.Create;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.user.model.User;
//
//import javax.validation.constraints.NotBlank;
//import java.time.LocalDateTime;
//
//@Data
//@Builder
//@AllArgsConstructor
//public class CommentDtoRequest {
//    private Long id;
//    @NotBlank(groups = {Create.class})
//    private String text;
//    private Item item;
//    private User user;
//    private LocalDateTime created = LocalDateTime.now();
//}
