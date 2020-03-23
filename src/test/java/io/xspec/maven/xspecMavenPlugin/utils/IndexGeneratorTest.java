/**
 * Copyright © 2019, Christophe Marchand, XSpec organization
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.xspec.maven.xspecMavenPlugin.utils;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author cmarchand
 */
public class IndexGeneratorTest {
    private final RunnerOptions options = new RunnerOptions(new File("target/tests"));

    @Test
    public void constructorTest() {
        IndexGenerator gen = new IndexGenerator(options, Collections.EMPTY_LIST);
        assertEquals("options are not set", this.options, gen.options);
        assertEquals("processedFiles are not set", 0, gen.processedFiles.size());
    }
    
    @Test
    public void indexFileTest() throws Exception {
        IndexGenerator gen = new IndexGenerator(options, Collections.EMPTY_LIST);
        gen.generateIndex();
        File expected = new File(options.reportDir, "index.html");
        assertTrue("index file not generated", expected.exists());
        assertTrue("index file is not a file", expected.isFile());
        long nbLines = 38;
        assertEquals("index should have "+nbLines+" lines", nbLines, Files.lines(expected.toPath()).count());
    }
    
    @Test
    public void tableContentTest() throws Exception {
        ProcessedFile pf = new ProcessedFile(
                options.testDir, 
                new File(options.testDir, "toto.xspec"),
                options.reportDir, 
                new File(options.reportDir, "toto.xspec.html"));
        pf.setResults(10, 1, 1, 1, 13);
        IndexGenerator gen = new IndexGenerator(options, Arrays.asList(pf));
        gen.generateIndex();
        File expected = new File(options.reportDir, "index.html");
        long nbLines = 39 + 3 + 8;
        assertEquals("index should have "+nbLines+" lines", nbLines, Files.lines(expected.toPath()).count());
        assertEquals("table should contain 1 red row", 1, Files.lines(expected.toPath()).filter(l -> l.contains("class=\"error\"")).count());
    }
}