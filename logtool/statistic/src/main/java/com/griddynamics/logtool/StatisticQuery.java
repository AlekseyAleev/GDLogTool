package com.griddynamics.logtool;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: avyshkin
 * Date: 10.02.12
 * Time: 13:26
 * To change this template use File | Settings | File Templates.
 */
public class StatisticQuery {
    private Map<String,String> query;
    private Integer stepDiscretization;
    private String startDate;
    private String endDate;
    /*
    *   startDate and endDate is date in solr date format
    *   stepDiscretization - is a number of parts into which time period divided
    */
    public StatisticQuery(Map<String,String> query,Integer stepDiscretization,String startDate,String endDate){
        this.query = query;
        this.stepDiscretization = stepDiscretization;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public Map<String,String> getQuery(){
        return query;
    }
    public Integer getStepDiscretization(){
        return stepDiscretization;
    }
    public String getStartDate(){
        return startDate;
    }
    public String getEndDate(){
        return endDate;
    }
}
