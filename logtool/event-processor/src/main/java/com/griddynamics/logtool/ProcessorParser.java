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

    static Map<String,String> props = new HashMap();
    static Map processors = new Properties();

    public static String processorConfigParser(String processor) throws MalformedPatternException {
        String result = "";
        //
        String regexp1 = "(.*)\\s*=(.*)";
        PatternCompiler compiler = new Perl5Compiler();
        Pattern pattern = compiler.compile(regexp1);
        PatternMatcher matcher = new Perl5Matcher();
        //
        if (matcher.contains(processor,pattern)) {
            MatchResult res=matcher.getMatch();
            //System.out.println(res.group(1));
            //System.out.println(res.group(2));
            result = MakeRegExp(res.group(2));
            processors.put(res.group(1)," "+result);
        }else{
            System.out.println("Ups");
        }
        return result;
    }

    public static String MakeRegExp(String processor) throws MalformedPatternException{
        String regexp1 = "@(.+?);";
        int begin = 0;
        PatternCompiler compiler = new Perl5Compiler();
        Pattern pattern = compiler.compile(regexp1);
        PatternMatcher matcher = new Perl5Matcher();

        while (matcher.contains(processor.substring(begin,processor.length()),pattern)) {
            MatchResult res=matcher.getMatch();
            //System.out.println(res.group(0));
            //System.out.println("1-"+processor);
            if (processor.charAt(begin + res.beginOffset(1) - 2) != '\\') {
                processor = StringUtils.replace(processor,"@"+res.group(1)+";",props.get(res.group(1)));
                //System.out.println("2"+processor);
                begin =begin + props.get(res.group(1)).length();
            }else{
                begin =begin + res.endOffset(1);
            }
        }

        return processor;
    }


    public static void main(String[] args) throws IOException, MalformedPatternException {
        //ArrayList<String> list = new ArrayList<String>();
        //Hashtable tokensList = new Hashtable ();
        File processor = new File("patternLib.conf");
        Scanner procScan = new Scanner(processor);
        File token = new File("token.conf");
        Scanner tokenScan = new Scanner(token);
        while (tokenScan.hasNextLine()) {
            String tokenStr = tokenScan.nextLine();
            tokenStr = tokenStr.replace("\\", "\\\\");
            StringTokenizer delim = new StringTokenizer(tokenStr, "=");
            props.put(delim.nextToken(), delim.nextToken());
        }
        //props.list(System.out);
        while (procScan.hasNextLine()) {
            String input = procScan.nextLine();
            input = input.replace("\\", "\\\\");
            String out = processorConfigParser(input);
            System.out.println(out);
        }
    }

}
