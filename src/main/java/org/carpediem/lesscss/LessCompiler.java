/*
 *  Copyright 2010 Richard Nichols.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package org.carpediem.lesscss;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.ScriptableObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

final public class LessCompiler {
    private final Context context;
    private final ScriptableObject scriptableObject;
    private final String LESS_SRC = "org/carpediem/lesscss/less-1.1.5.min.js";
    private final String RUN_SRC = "org/carpediem/lesscss/run.js";

    public LessCompiler() throws IOException {
        final InputStream lessIs = getClass().getClassLoader().getResourceAsStream(LESS_SRC);
        final InputStream runIs = getClass().getClassLoader().getResourceAsStream(RUN_SRC);
        final ContextFactory contextFactory = new ContextFactory();
        context = contextFactory.enterContext();
        scriptableObject = context.initStandardObjects();
        context.setOptimizationLevel(9);
        context.evaluateString(scriptableObject, asString(lessIs), LESS_SRC, 1, null);
        context.evaluateString(scriptableObject, asString(runIs), RUN_SRC, 1, null);
        lessIs.close();
        runIs.close();
    }

    public String compile(InputStream input) throws IOException {
        String data = asString(input);
        final String replace = data.replace("\"", "\\\"").replaceAll("\n", "").replaceAll("\r", "");
        StringBuilder sb = new StringBuilder("lessIt(\"").append(replace).append("\")");
        String lessitjs = sb.toString();
        return context.evaluateString(scriptableObject, lessitjs, "lessitjs.js", 1, null).toString();
    }

    private String asString(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
