package com.sqshq.flattener;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FlattenerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    private final Flattener flattener = new Flattener(mapper);

    @Test
    void shouldFlattenSingleNestedObject() throws IOException {
        //given
        var input = getResourceStream("simple-nested-input.json");

        //when
        var result = flattener.flatten(input);

        //then
        assertEquals(getResourceString("simple-nested-result.json"), result);
    }

    @Test
    void shouldFlattenDeepNestedObject() throws IOException {
        //given
        var input = getResourceStream("deep-nested-input.json");

        //when
        var result = flattener.flatten(input);

        //then
        assertEquals(getResourceString("deep-nested-result.json"), result);
    }

    @Test
    void shouldPreserveAlreadyNestedJson() throws IOException {
        //given
        var input = getResourceStream("simple-nested-result.json");

        //when
        var result = flattener.flatten(input);

        //then
        assertEquals(getResourceString("simple-nested-result.json"), result);
    }

    @Test
    void shouldNotFailOnEmptyInput() throws IOException {
        //given
        var input = "";

        //when
        var result = flattener.flatten(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        //then
        assertEquals(result, input);
    }

    @Test
    void shouldFailOnCorruptedInput() {
        //given
        var input = getResourceStream("corrupted-input.json");

        //when
        //then
        assertThrows(JsonParseException.class, () -> flattener.flatten(input));
    }

    @Test
    void shouldFailIfInputContainsArray() {
        //given
        var input = getResourceStream("array-input.json");

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> flattener.flatten(input));
    }

    private InputStream getResourceStream(String name) {
        return getClass().getClassLoader().getResourceAsStream(name);
    }

    private String getResourceString(String name) throws IOException {
        var path = getClass().getClassLoader().getResource(name).getPath();
        return Files.readString(Paths.get(path), StandardCharsets.UTF_8);
    }
}
