package com.griddynamics.logtool;

import org.apache.oro.text.regex.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: aaleev
 * Date: 1/18/12
 * Time: 8:43 PM
 */

public class Processor {
    private static final Logger logger = LoggerFactory.getLogger(Processor.class);
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
            logger.error(e.getMessage(), e);
        }
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
