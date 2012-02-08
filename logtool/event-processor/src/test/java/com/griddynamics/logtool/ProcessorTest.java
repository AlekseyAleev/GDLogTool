package com.griddynamics.logtool;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: aaleev
 * Date: 1/19/12
 * Time: 8:32 PM
 */
public class ProcessorTest {
    @Test
    public void testConstructor() {
        Processor proc = new Processor("simpleProcessor", "randomnumber\\d+", "simpleTag");
        assertEquals("simpleProcessor", proc.getName());
        assertEquals("simpleTag", proc.getTag());
    }

    @Test
    public void testMatch() {
        Processor proc = new Processor("Processor", "randomNumber\\d+", "Tag");
        assertTrue(proc.match("randomNumber5"));
        assertTrue(proc.match("randomNumber20"));
        assertTrue(proc.match("randomNumber0"));
        assertFalse(proc.match("randomNumber"));
        assertFalse(proc.match("randomNumberFail"));
        assertFalse(proc.match("randomnumber1"));
        assertFalse(proc.match(""));
        assertFalse(proc.match("555"));
        assertFalse(proc.match("abcdefg"));
        assertFalse(proc.match(null));
    }

    @Test
    public void testTag() {
        Processor proc = new Processor("Processor", "\\d\\d\\d", "New tag");
        TaggedMessage msg = new TaggedMessage("This is message");
        assertEquals(0, msg.getTags().size());
        proc.tag(msg);
        assertEquals(1, msg.getTags().size());
        assertEquals("New tag", msg.getTags().get(0));
    }
}
