package com.griddynamics.logtool;

import org.apache.oro.text.regex.*;

/**
 * Created by IntelliJ IDEA.
 * User: aaleev
 * Date: 1/18/12
 * Time: 8:43 PM
 */

public class Processor {
    private String myName;
    private Pattern myPattern;
    private PatternMatcher myMatcher;
    private String myTag; //TODO: Think of tag's type - String or something else (it also affects on TaggedMessage class)
    
    public Processor(String name, String pattern, String tag) {
        this.myName = name;
        this.myTag = tag;
        this.myMatcher = new Perl5Matcher();
        try {
            this.myPattern = new Perl5Compiler().compile(pattern);
        } catch(MalformedPatternException e) {
        }
    }

    public String getName() {
        return myName;
    }

    public Pattern getPattern() {
        return myPattern;
    }

    public String getTag() {
        return myTag;
    }

    public PatternMatcher getMatcher() {
        return myMatcher;
    }

    public boolean match(String msg) {
        return msg != null && myMatcher.matches(msg, myPattern);
    }


    public void tag(TaggedMessage taggedMessage) {
        taggedMessage.addTag(myTag);
    }

}
