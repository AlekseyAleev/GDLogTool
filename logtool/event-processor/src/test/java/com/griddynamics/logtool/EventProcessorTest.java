package com.griddynamics.logtool;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: aaleev
 * Date: 1/19/12
 * Time: 7:49 PM
 */
public class EventProcessorTest {
    @Test
    public void testProcess() {
        EventProcessor ep = new EventProcessor();
        for (int i = 1; i <= 5; ++i) {
            if (i % 2 == 0) {
                ep.addProcessor(new Processor("Processor " + i, "even", "Tag " + i));
            } else {
                ep.addProcessor(new Processor("Processor " + i, "odd", "Tag " + i));
            }
        }

        String evenMsg = "even";
        String oddMsg = "odd";
        TaggedMessage evenTaggedMsg = ep.process(evenMsg);
        TaggedMessage oddTaggedMsg = ep.process(oddMsg);

        assertEquals(evenMsg, evenTaggedMsg.getMessage());
        assertEquals(2, evenTaggedMsg.getTags().size());
        assertEquals("Tag 2", evenTaggedMsg.getTags().get(0));
        assertEquals("Tag 4", evenTaggedMsg.getTags().get(1));

        assertEquals(oddMsg, oddTaggedMsg.getMessage());
        assertEquals(3, oddTaggedMsg.getTags().size());
        assertEquals("Tag 1", oddTaggedMsg.getTags().get(0));
        assertEquals("Tag 3", oddTaggedMsg.getTags().get(1));
        assertEquals("Tag 5", oddTaggedMsg.getTags().get(2));
    }

    @Test
    public void testPutTagsToMap() {
        TaggedMessage msg = new TaggedMessage("Message");
        msg.addTag("unknownHost");
        msg.addTag("unknownThread");
        msg.addTag("unknownTime");
        msg.addTag("SimpleMessage");
        Map<String, String> map = new HashMap<String, String>();

        EventProcessor.putTagsToMap(msg, map);

        assertEquals(4, map.values().size());

        assertTrue(map.keySet().contains("tag1"));
        assertTrue(map.keySet().contains("tag2"));
        assertTrue(map.keySet().contains("tag3"));
        assertTrue(map.keySet().contains("tag4"));

        assertTrue(map.values().contains("unknownHost"));
        assertTrue(map.values().contains("unknownThread"));
        assertTrue(map.values().contains("unknownTime"));
        assertTrue(map.values().contains("SimpleMessage"));
    }
}
