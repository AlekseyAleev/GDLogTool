package com.griddynamics.logtool;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: aaleev
 * Date: 1/19/12
 * Time: 7:49 PM
 */
public class EventProcessorTest {
    @Test
    public void processTest() {
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
        
        assertEquals(evenTaggedMsg.getMessage(), evenMsg);
        assertEquals(evenTaggedMsg.getTags().size(), 2);
        assertEquals(evenTaggedMsg.getTags().get(0), "Tag 2");
        assertEquals(evenTaggedMsg.getTags().get(1), "Tag 4");

        assertEquals(oddTaggedMsg.getMessage(), oddMsg);
        assertEquals(oddTaggedMsg.getTags().size(), 3);
        assertEquals(oddTaggedMsg.getTags().get(0), "Tag 1");
        assertEquals(oddTaggedMsg.getTags().get(1), "Tag 3");
        assertEquals(oddTaggedMsg.getTags().get(2), "Tag 5");
    }
}
