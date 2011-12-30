package org.petrovic.lesscss;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class LessCompilerTest {

    @Test
    public void testApp() throws IOException {
        String input = "@color: #4D926F; #header { color: @color; } h2 { color: @color; }";
        LessCompiler lessCompiler = new LessCompiler();
        final String compile = lessCompiler.compile(new ByteArrayInputStream(input.getBytes("UTF-8")));
        System.out.println(compile);
    }
}
