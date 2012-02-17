package com.griddynamics.logtool;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.oro.text.regex.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: avyshkin
 * Date: 31.01.12
 * Time: 12:57
 * To change this template use File | Settings | File Templates.
 */
public class ConfigLoader {
    private String fileName;
    private List<Pair<String,String>> configList = new LinkedList<Pair<String, String>>();
    public ConfigLoader(String fileName){
        this.fileName = fileName;
    }
    public List<Pair<String, String>> loadingLikeListOfPairs() throws IOException, MalformedPatternException{
        String path = new File("").getAbsolutePath();
        String fs = System.getProperty("file.separator");
        String confPath = path + fs + fileName;
        File tokenFileStream = new File(confPath);
        Scanner tokenScanner;
        if (!tokenFileStream.exists()) {
            InputStream in = ConfigLoader.class.getResourceAsStream(System.getProperty("file.separator") + fileName);
            tokenScanner = new Scanner(in);
        } else if (!tokenFileStream.isFile()) {
            throw new IOException("config file exists and not a file");
        } else {
            tokenScanner = new Scanner(tokenFileStream);
        }


        String regexp = "(.*?)\\s*?[=:-]\\s*(.*)";
        PatternCompiler compiler = new Perl5Compiler();
        Pattern pattern = compiler.compile(regexp);
        PatternMatcher matcher = new Perl5Matcher();
        while (tokenScanner.hasNextLine()) {
            String input = tokenScanner.nextLine();
            if (matcher.contains(input,pattern)){
                MatchResult res=matcher.getMatch();

                configList.add(new MutablePair<String, String>(res.group(1), res.group(2)));
            }
        }
        return configList;
    }
}
