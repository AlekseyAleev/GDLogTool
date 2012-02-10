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
    private Map map;
    private Integer stepDiscretization;
    private String startDate;
    private String endDate;
    /*
    *   startDate and endDate is date in solr date format
    *   stepDiscretization - is a number of parts into which time period divided
    */
    public StatisticQuery(Map map,Integer stepDiscretization,String startDate,String endDate){
        this.map = map;
        this.stepDiscretization = stepDiscretization;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public Map getMap(){
        return map;
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
