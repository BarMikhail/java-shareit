package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {
    private long id;
    private String name;
    private String email;
}
