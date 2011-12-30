package com.griddynamics.logtool;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: aaleev
 * Date: 12/23/11
 * Time: 6:35 PM
 */
public class EventProcessor {
    static Properties props = new Properties();
    public static String processorConfigParser(String processor) {
        String result = "";
        for (int i = 0; i < processor.length() - 1; i++) {
            if (!processor.substring(i, i + 1).equals("@")) {
                result = result + processor.substring(i, i + 1);
                if (processor.substring(i, i + 1).equals("\\")){
                    result = result + "\\";
                }
            } else {
                String token = "";
                int j;
                for (j = i; !processor.substring(j, j + 1).equals(";") && j < processor.length() - 1; j++) {}
                if (processor.substring(j, j + 1).equals(";")){
                    token = token + processor.substring(i + 1, j);
                    //System.out.println(token);
                    token = props.getProperty(token);
                    result = result + token;
                }else{
                    for (int k = i;k < j  + 1;k++){
                        if (processor.substring(k, k + 1).equals("\\")){
                            result = result + processor.substring(k,k+1) + "\\";
                        } else {
                            result = result + processor.substring(k,k+1);
                        }
                    }
                }
                i = j;
            }

        }
        return result;
    }

}
