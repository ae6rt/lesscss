package org.petrovic.lesscss;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.ScriptableObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LessCompiler {

    private Context context;
    private ScriptableObject scriptableObject;

    public static void main(String[] args) throws IOException {
        try {
            LessCompiler lessCompiler = new LessCompiler();
            String t = lessCompiler.compile(new FileInputStream("style.less"));
            System.out.print("result: " + t);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public LessCompiler() {
        String lessSrc = "less-1.6.1.js";
        String runSrc = "run.js";

        final InputStream lessIs = getClass().getClassLoader().getResourceAsStream(lessSrc);
        final InputStream runIs = getClass().getClassLoader().getResourceAsStream(runSrc);
        try {
            final ContextFactory contextFactory = new ContextFactory();
            context = contextFactory.enterContext();
            scriptableObject = context.initStandardObjects();
            context.setOptimizationLevel(9);

            context.evaluateString(scriptableObject, asString(lessIs), lessSrc, 1, null);
            context.evaluateString(scriptableObject, asString(runIs), runSrc, 1, null);
            lessIs.close();
            runIs.close();
        } catch (IOException ex) {
            throw new IllegalStateException("Failed reading javascript less.js", ex);
        }
    }

    public String compile(InputStream input) throws IOException {
        String data = asString(input);
        final String replace = data.replace("\"", "\\\"").replaceAll("\n", "").replaceAll("\r", "");
        StringBuilder sb = new StringBuilder("lessIt(\"").append(replace).append("\")");
        String lessitjs = sb.toString();
        String result = context.evaluateString(scriptableObject, lessitjs, "lessitjs.js", 1, null).toString();
        return result;
    }

    private String asString(InputStream inputStream) throws IOException {
        String line;
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        String s = sb.toString();
        return s;
    }
}
