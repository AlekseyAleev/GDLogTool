package com.griddynamics.logtool;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.oro.text.regex.*;

/**
 * Created by IntelliJ IDEA.
 * User: avyshkin
 * Date: 30.12.11
 * Time: 12:13
 * To change this template use File | Settings | File Templates.
 */
public class ProcessorParser {

    private Map<String,String> tokens = new HashMap();
    private Map<String,String> processors = new HashMap();

    private String processorConfigParser(String processor) throws MalformedPatternException{
        String regexp1 = "@(.+?);";
        int begin = 0;
        PatternCompiler compiler = new Perl5Compiler();
        Pattern pattern = compiler.compile(regexp1);
        PatternMatcher matcher = new Perl5Matcher();

        while (matcher.contains(processor.substring(begin,processor.length()),pattern)) {
            MatchResult res=matcher.getMatch();
            if (processor.charAt(begin + res.beginOffset(1) - 2) != '\\') {
                processor = StringUtils.replace(processor,"@"+res.group(1)+";",tokens.get(res.group(1)));
                begin =begin + tokens.get(res.group(1)).length();
            }else{
                begin =begin + res.endOffset(1);
            }
        }

        return processor;
    }
    //read all Tokens and put them to the map
    private void loadTokenConfig(String tokenFile) throws IOException{
        File token = new File(tokenFile);
        Scanner tokenScanner = new Scanner(token);
        while (tokenScanner.hasNextLine()) {
            String tokenStr;
            tokenStr = tokenScanner.nextLine();
            tokenStr = tokenStr.replace("\\", "\\\\");
            StringTokenizer tokenized = new StringTokenizer(tokenStr, "=");
            tokens.put(tokenized.nextToken(), tokenized.nextToken());
        }
    }
    //end token reading
    //read processors and replace all tokens("@<token name>;") to their meaning
    private void loadProcessors(String processorFile) throws IOException, MalformedPatternException{
        File processor = new File(processorFile);
        Scanner processorScanner = new Scanner(processor);
        while (processorScanner.hasNextLine()) {
            String input = processorScanner.nextLine();
            StringTokenizer tokenized = new StringTokenizer(input, "=");
            tokens.put(tokenized.nextToken(), processorConfigParser(tokenized.nextToken().replace("\\", "\\\\")));
        }
    }
    //end processors reading

    public void load(String processorFile, String tokenFile) throws IOException, MalformedPatternException {
        loadTokenConfig(tokenFile);
        loadProcessors(processorFile);

    }
}

