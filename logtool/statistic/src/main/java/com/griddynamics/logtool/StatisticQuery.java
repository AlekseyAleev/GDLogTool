package com.griddynamics.logtool;

import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: avyshkin
 * Date: 10.02.12
 * Time: 13:26
 * To change this template use File | Settings | File Templates.
 */
public class StatisticQuery {
    private LinkedHashMap<String,Object> map;
    private Integer stepDiscretization;
    private String startDate;
    private String endDate;
    public StatisticQuery(LinkedHashMap map,Integer stepDiscretization,String startDate,String endDate){
        this.map = map;
        this.stepDiscretization = stepDiscretization;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public LinkedHashMap<String,Object> getMap(){
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
