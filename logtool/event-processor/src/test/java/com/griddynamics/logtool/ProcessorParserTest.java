package com.griddynamics.logtool;

import java.io.File;
import java.io.IOException;
import java.util.*;

import junit.framework.Assert;
import org.apache.commons.lang3.StringUtils;
import org.apache.oro.text.regex.*;
import org.junit.*;

/**
 * Created by IntelliJ IDEA.
 * User: avyshkin
 * Date: 18.01.12
 * Time: 20:05
 * To change this template use File | Settings | File Templates.
 */
public class ProcessorParserTest extends Assert{

    private Map<String,String> processorsActual = new HashMap();
    private Map<String,String> processorsExpected = new HashMap();
    private String processorFile = "patternLib.conf";
    private String tokenFile = "token.conf";
    private ProcessorParser parserTest = new ProcessorParser();
    @Before
    public void initial(){
        processorsExpected.put("proc ","\\d\\d:\\d\\d:\\d\\d\\sthis.is.host\\s\\(\\d*\\)\\s[\\w\\d\\s]+\\s\\@file;:\\d:Exception[\\w\\d\\s:]+[a-z]*;[a-z]*oneMoreRegExp\\@eof");
        processorsExpected.put("pro ","\\d:this.is.host\\sololo");
        processorsExpected.put("procTest1 ","\\@some_reg\\s\\d\\d:\\d\\d:\\d\\d\\(\\d*\\)\\@ups;");
        processorsExpected.put("procTest2 ","@host\\(\\d*\\)\\@sometext1;\\@sometext2;");
    }
    @Test
    public void testProcessorConfigParser () throws IOException,MalformedPatternException{
        processorsActual = parserTest.load(processorFile,tokenFile);
        assertEquals(processorsExpected,processorsActual);
        String temp = processorsActual.get("pro ");
        PatternCompiler compiler = new Perl5Compiler();
        Pattern pattern = compiler.compile(temp);
        PatternMatcher matcher = new Perl5Matcher();
        String message = "5:this.is.host ololo";
        assertTrue(matcher.contains(message,pattern));

    }
    @After
    public void tearDownProcessorConfigParser(){
        processorsActual.clear();
        processorsExpected.clear();
    }
}
