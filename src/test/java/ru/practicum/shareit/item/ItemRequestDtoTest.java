package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void testSerialize() throws Exception {
        ItemRequestDto itemDto = ItemRequestDto.builder()
                .name("Test")
                .description("test test")
                .available(true)
                .requestId(1L)
                .build();

        JsonContent<ItemRequestDto> itemDtoSaved = json.write(itemDto);


        assertThat(itemDtoSaved).hasJsonPath("$.name");
        assertThat(itemDtoSaved).hasJsonPath("$.description");
        assertThat(itemDtoSaved).hasJsonPath("$.available");
        assertThat(itemDtoSaved).hasJsonPath("$.requestId");


        assertThat(itemDtoSaved).extractingJsonPathStringValue("$.name").isEqualTo("Test");
        assertThat(itemDtoSaved).extractingJsonPathStringValue("$.description").isEqualTo("test test");
        assertThat(itemDtoSaved).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(itemDtoSaved).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

}