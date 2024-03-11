package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void ItemDtoTest() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Test")
                .description("test test")
                .available(true)
                .owner(1L)
                .requestId(1L)
                .build();

        JsonContent<ItemDto> itemDtoSaved = json.write(itemDto);

        assertThat(itemDtoSaved).hasJsonPath("$.id");
        assertThat(itemDtoSaved).hasJsonPath("$.name");
        assertThat(itemDtoSaved).hasJsonPath("$.description");
        assertThat(itemDtoSaved).hasJsonPath("$.available");
        assertThat(itemDtoSaved).hasJsonPath("$.owner");
        assertThat(itemDtoSaved).hasJsonPath("$.requestId");

        assertThat(itemDtoSaved).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(itemDtoSaved).extractingJsonPathStringValue("$.name").isEqualTo("Test");
        assertThat(itemDtoSaved).extractingJsonPathStringValue("$.description").isEqualTo("test test");
        assertThat(itemDtoSaved).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(itemDtoSaved).extractingJsonPathNumberValue("$.owner").isEqualTo(1);
        assertThat(itemDtoSaved).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }
}