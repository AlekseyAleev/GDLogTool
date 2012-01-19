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
    private String myMessage;
    private List<String> myTags; //TODO: Think of tags' type - String or something else.
    
    public TaggedMessage(String msg) {
        this.myMessage = msg;
        this.myTags = new LinkedList<String>();
    }
    
    public List<String> getTags() {
        return myTags;
    }
    
    public void addTag(String tag) {
        myTags.add(tag);
    }
    
    public String getMessage() {
        return myMessage;
    }
    
    public void setMessage(String msg) {
        myMessage = msg;
    }
}
