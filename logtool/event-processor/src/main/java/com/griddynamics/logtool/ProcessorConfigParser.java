package com.griddynamics.logtool;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.*;
import org.apache.oro.text.regex.*;

/**
 * Created by IntelliJ IDEA.
 * User: avyshkin
 * Date: 30.12.11
 * Time: 12:13
 * To change this template use File | Settings | File Templates.
 */
public class ProcessorConfigParser {

    private Map<String,String> tokens = new HashMap();
    //private Map<String,String> processors = new HashMap();
    private List<Processor> processors = new LinkedList<Processor>();

    private String processorConfigParsing(String processor) throws MalformedPatternException{
        String regexp1 = "@(\\w+?);";
        int begin = 0;
        PatternCompiler compiler = new Perl5Compiler();
        Pattern pattern = compiler.compile(regexp1);
        PatternMatcher matcher = new Perl5Matcher();

        while (matcher.contains(processor.substring(begin,processor.length()),pattern)) {
            MatchResult res=matcher.getMatch();
            if ((begin + res.beginOffset(1) - 2 < 0)||(processor.charAt(begin + res.beginOffset(1) - 2) != '\\')) {
                processor = StringUtils.replace(processor,"@"+res.group(1)+";",tokens.get(res.group(1)));
                begin =begin + tokens.get(res.group(1)).length();
            }else{
                begin =begin + res.endOffset(1);
            }
        }
        return processor;
    }
    //read all Tokens and put them to the map
    private void loadingTokenConfig(String tokenFile) throws IOException, MalformedPatternException{
        ConfigLoader loader = new ConfigLoader(tokenFile);
        List<Pair<String, String>> configList = loader.loadingLikeListOfPairs();
        for (Pair<String, String> tokenPair: configList){
            tokens.put(tokenPair.getLeft(),tokenPair.getRight());
        }
    }
    //end token reading

    //read processors and replace all tokens("@<token name>;") to their meaning
    private void loadingProcessors(String processorFile) throws IOException, MalformedPatternException{
        ConfigLoader loader = new ConfigLoader(processorFile);
        List<Pair<String, String>> configList = loader.loadingLikeListOfPairs();
        for (Pair<String, String> processorPair: configList){
            String processorParsedRegExp = processorConfigParsing(processorPair.getRight());
            String processorName = processorPair.getLeft();
            processors.add(new Processor(processorName,processorParsedRegExp,processorName));
        }
    }
    //end processors reading

    public List<Processor> load(String processorFile, String tokenFile) throws IOException, MalformedPatternException {
        loadingTokenConfig(tokenFile);
        loadingProcessors(processorFile);
        return processors;
    }
}

