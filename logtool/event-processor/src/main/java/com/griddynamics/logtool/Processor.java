package com.griddynamics.logtool;

/**
 * Created by IntelliJ IDEA.
 * User: aaleev
 * Date: 1/18/12
 * Time: 8:43 PM
 */

public class Processor {
    private String myName;
    private String myPattern;
    private String myTag; //TODO: Think of tag's type - String or something else (it also affects on TaggedMessage class)
    
    public Processor(String name, String pattern, String tag) {
        this.myName = name;
        this.myPattern = pattern;
        this.myTag = tag;
    }
    
    public void setName(String name) {
        this.myName = name;
    }
    
    public String getName() {
        return myName;
    }

    public void setPattern(String pattern) {
        this.myPattern = pattern;
    }

    public String getPattern() {
        return myPattern;
    }
    

    public void setTag(String tag) {
        myTag = tag;
    } 
    
    public String getTag() {
        return myTag;
    }

    public boolean match(String msg) {
        //TODO: Implement
        return true;
    }


    public void tag(TaggedMessage taggedMessage) {
        taggedMessage.addTag(myTag);
    }

}
