package ru.practicum.shareit.request;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemPostDto;

@JsonTest
class ItemPostDtoTest {

    @Autowired
    private JacksonTester<ItemPostDto> json;

    @Test
    void itemPostDtoTest() throws Exception {
        ItemPostDto requestDtoInput = ItemPostDto.builder()
                .description("Test !test test")
                .build();

        JsonContent<ItemPostDto> result = json.write(requestDtoInput);

        Assertions.assertThat(result).hasJsonPath("$.description");
        Assertions.assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Test !test test");
    }

}