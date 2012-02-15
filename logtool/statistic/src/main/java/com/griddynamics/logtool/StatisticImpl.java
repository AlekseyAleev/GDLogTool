package com.griddynamics.logtool;

import org.apache.solr.schema.DateField;

import java.util.*;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by IntelliJ IDEA.
 * User: avyshkin
 * Date: 10.02.12
 * Time: 16:29
 * To change this template use File | Settings | File Templates.
 */
public class StatisticImpl implements Statistic{
    private SearchServer searchServer;
    private final DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    public StatisticImpl(SearchServer searchServer){
        this.searchServer = searchServer;
    }
    private String makeRequest(Map<String,List<String>> map,long startTime,long endTime){
        String query = "";
        for (String key : map.keySet()) {
          String oneTagQuery = "";
          for (String value : map.get(key)) {
            oneTagQuery += key + ":" + value + " AND ";
          }
          query += oneTagQuery;
        }
        DateTime dt = new DateTime(startTime);
        String startTimeString = timeFormatter.print(dt);
        dt = new DateTime(endTime);
        String endTimeString = timeFormatter.print(dt);
        query = query + "timestamp:[" + startTimeString + " TO " + endTimeString +  "]";
        return query;
    }
    public StatisticResult makeStatistic(StatisticQuery query){
        List<Integer> result = new LinkedList<Integer>();
        List<String> time = new LinkedList<String>();
        DateTime startTime = new DateTime(query.getStartDate());
        DateTime endTime = new DateTime(query.getEndDate());
        long startMillis = startTime.getMillis();
        long endMillis = endTime.getMillis();
        long timePart = (endMillis - startMillis) / query.getStepDiscretization();
        for (long i = startMillis; i < endMillis; i += timePart){
            DateTime timeForList = new DateTime(i+timePart/2);
            time.add(timeFormatter.print(timeForList));
            List<Map<String,String>> list = searchServer.search(makeRequest(query.getQuery(),i,i + timePart),-1,-1,"timestamp","asc");
            result.add(new Integer(list.size()));
        }
        return new StatisticResult(result,time);
    }
}
