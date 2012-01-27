package com.griddynamics.logtool;

/**
 * Created by IntelliJ IDEA.
 * User: aaleev
 * Date: 1/19/12
 * Time: 3:33 PM
 */

import java.util.LinkedList;
import java.util.List;

public class TaggedMessage {
    private String message;
    private List<String> tags; //TODO: Think of tags' type - String or something else.
    
    public TaggedMessage(String msg) {
        this.message = msg;
        this.tags = new LinkedList<String>();
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void addTag(String tag) {
        tags.add(tag);
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String msg) {
        message = msg;
    }
}
