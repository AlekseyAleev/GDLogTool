package com.griddynamics.logtool;

import org.apache.solr.schema.DateField;

import java.util.*;

import org.joda.time.*;

/**
 * Created by IntelliJ IDEA.
 * User: avyshkin
 * Date: 10.02.12
 * Time: 16:29
 * To change this template use File | Settings | File Templates.
 */
public class StatisticImpl implements Statistic{
    private SearchServer searchServer;
    public StatisticImpl(SearchServer searchServer){
        this.searchServer = searchServer;
    }
    private String makeRequest(Map<String,String> map){
        String temp = "";
        for(String fieldName: map.keySet()){
        //here we shoud make request from map
            temp = temp + map.get(fieldName);
        }
        return temp;
    }
    public StatisticResult makeStatistic(StatisticQuery query){
        List<Integer> result = new LinkedList<Integer>();
        DateTime startTime = new DateTime(query.getStartDate());
        DateTime endTime = new DateTime(query.getEndDate());
        long timePart = (startTime.getMillis() - endTime.getMillis()) / query.getStepDiscretization();

        for (long i= startTime.getMillis();i<endTime.getMillis();i = i + timePart){
            List<Map<String,String>> list = searchServer.search(makeRequest(query.getQuery()),-1,-1,"","");
            result.add(new Integer(list.size()));
        }
        return new StatisticResult(result);
    }
}
