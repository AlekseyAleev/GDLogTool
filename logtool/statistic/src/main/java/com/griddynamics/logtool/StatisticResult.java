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
    private List<Integer> resultList;
    private List<String> time;
    public StatisticResult(List<Integer> resultList,List<String> time){
        this.resultList = resultList;
        this.time = time;
    }
    public List<Integer> getStatistic(){
        return resultList;
    }
    public List<String> getTime(){
        return time;
    }
}
