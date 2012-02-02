package com.griddynamics.logtool;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: aaleev
 * Date: 1/29/12
 * Time: 11:34 AM
 */
public class EventProcessorFactory {
    public EventProcessor getEventProcessor() {
        //Dummy implementation
        List<Processor> processors = new LinkedList<Processor>();
        Processor proc1 = new Processor("InfoProcessor", ".*INFO.*", "InfoMessage");
        Processor proc2 = new Processor("ErrorProcessor", ".*ERROR.*", "ErrorMessage");
        processors.add(proc1);
        processors.add(proc2);
        return new EventProcessor(processors);
    }
}
