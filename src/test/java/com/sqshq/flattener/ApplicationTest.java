package com.sqshq.flattener;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicationTest {

    private final InputStream stdin = System.in;
    private final PrintStream stdout = System.out;
    private ByteArrayOutputStream testout;

    @BeforeEach
    void setUpOutput() {
        testout = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testout));
    }

    @AfterEach
    void restoreSystemInputOutput() {
        System.setIn(stdin);
        System.setOut(stdout);
    }

    @Test
    void shouldAcceptStdinAndWriteToStdout() throws IOException {
        //given
        var input = "{ }" + System.lineSeparator();
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        //when
        Application.main(new String[0]);

        //then
        assertEquals(input, testout.toString());
    }
}