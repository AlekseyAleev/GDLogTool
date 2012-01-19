package com.griddynamics.logtool;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: aaleev
 * Date: 12/23/11
 * Time: 6:35 PM
 */

public class EventProcessor {
    private List<Processor> myProcessors;

    public EventProcessor() {
        this.myProcessors = new LinkedList<Processor>();
    }
    
    public EventProcessor(List<Processor> processors) {
        this.myProcessors = processors;
    }
    
    public void addProcessor(Processor processor) {
        myProcessors.add(processor);
    }

    public List<Processor> getProcessors() {
        return myProcessors;
    }

    /**
     * This method run log message through a chain of processors which match message with their patterns and add
     * their tags if it required.
     *
     * @param msg: log message
     * @return taggedMessage: message with list of tags of processors which patterns was matched with this message
     */
    public TaggedMessage process(String msg) {
        TaggedMessage taggedMessage = new TaggedMessage(msg);
        for (Processor processor : myProcessors) {
            if (processor.match(msg)) {
                processor.tag(taggedMessage);
            }
        }

        return taggedMessage;
    }
}
