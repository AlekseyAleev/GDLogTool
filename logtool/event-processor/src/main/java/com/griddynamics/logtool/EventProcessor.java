package com.griddynamics.logtool;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: aaleev
 * Date: 12/23/11
 * Time: 6:35 PM
 */

public class EventProcessor {
    private List<Processor> processors;

    public void addProcessor(Processor processor) {
        processors.add(processor);
    }
    
    public void setProcessors(List<Processor> processors) {
        this.processors = processors;
    }

    public List<Processor> getProcessors() {
        return processors;
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
        for (Processor processor : processors) {
            if (processor.match(msg)) {
                processor.tag(taggedMessage);
            }
        }

        return taggedMessage;
    }
}
