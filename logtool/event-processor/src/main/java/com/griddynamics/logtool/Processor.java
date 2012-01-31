package com.griddynamics.logtool;

import org.apache.oro.text.regex.*;

/**
 * Created by IntelliJ IDEA.
 * User: aaleev
 * Date: 1/18/12
 * Time: 8:43 PM
 */

public class Processor {
    private String name;
    private Pattern pattern;
    private PatternMatcher matcher;
    private String tag; //TODO: Think of tag's type - String or something else (it also affects on TaggedMessage class)
    
    public Processor(String name, String pattern, String tag) {
        this.name = name;
        this.tag = tag;
        this.matcher = new Perl5Matcher();
        try {
            this.pattern = new Perl5Compiler().compile(pattern);
        } catch(MalformedPatternException e) {
        }
    }
    public boolean equals(Object obj) {
        Processor proc= (Processor) obj;
        return ((this.name.equals(proc.getName())) && (this.tag.equals(proc.getTag())));
        //it is some shit but matchers and patterns are not equals because it is different objects
    }
    public int hashCode(){
        int hash = 0;
        hash = hash + this.name.hashCode()+this.tag.hashCode();
        return hash;
    }
    public String getName() {
        return name;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getTag() {
        return tag;
    }

    public PatternMatcher getMatcher() {
        return matcher;
    }

    public boolean match(String msg) {
        return msg != null && matcher.matches(msg, pattern);
    }


    public void tag(TaggedMessage taggedMessage) {
        taggedMessage.addTag(tag);
    }

}
