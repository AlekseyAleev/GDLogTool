package com.griddynamics.logtool;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: avyshkin
 * Date: 13.02.12
 * Time: 17:34
 * To change this template use File | Settings | File Templates.
 */
public class StatisticResult {
    public List<Integer> resultList;
    public StatisticResult(List<Integer> resultList){
        this.resultList = resultList;
    }
    public List<Integer> returnAsList(){
        return resultList;
    }
}
