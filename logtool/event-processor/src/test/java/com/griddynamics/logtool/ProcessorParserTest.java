package com.griddynamics.logtool;

import java.io.IOException;
import java.util.*;

import junit.framework.Assert;
import org.apache.oro.text.regex.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: avyshkin
 * Date: 18.01.12
 * Time: 20:05
 * To change this template use File | Settings | File Templates.
 */
public class ProcessorParserTest {

    private List<Processor> processorsActual = new LinkedList<Processor>();
    private List<Processor> processorsExpected = new LinkedList<Processor>();
    private String processorFile = "patternLib.conf";
    private String tokenFile = "token.conf";
    private ProcessorConfigParser parserTest = new ProcessorConfigParser();
    @Before
    public void initial(){
        Processor testProcessor = new Processor("pro", "\\d:this.is.host\\sololo","pro");
        processorsExpected.add(testProcessor);

        testProcessor = new Processor("proc",
                "\\d\\d:\\d\\d:\\d\\d\\sthis.is.host\\s\\(\\d*\\)\\s[\\w\\d\\s]+\\s\\@file;" +
                ":\\d:Exception[\\w\\d\\s:]+[a-z]*;[a-z]*oneMoreRegExp\\@eof","proc");
        processorsExpected.add(testProcessor);

        testProcessor = new Processor("procTest1", "\\@some_reg\\s\\d\\d:\\d\\d:\\d\\d\\(\\d*\\)\\@ups;","procTest1");
        processorsExpected.add(testProcessor);

        testProcessor = new Processor("procTest2", "@host=\\(\\d*\\)\\@sometext1;\\@sometext2;","procTest2");
        processorsExpected.add(testProcessor);
    }
    @Test
    public void testProcessorConfigParser () throws IOException,MalformedPatternException{
        processorsActual = parserTest.load(processorFile,tokenFile);
        //test for parsing
        assertTrue(processorsExpected.equals(processorsActual));
        //test end

    }
    @After
    public void tearDownProcessorConfigParser(){
        processorsActual.clear();
        processorsExpected.clear();
    }
}
