package com.griddynamics.logtool;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry.*;
/**
 * Created by IntelliJ IDEA.
 * User: avyshkin
 * Date: 08.02.12
 * Time: 17:11
 * To change this template use File | Settings | File Templates.
 */
public class StatisticResult {

    private List<Map.Entry<String, Integer>> statisticList;

    public StatisticResult(List<Map.Entry<String, Integer>> statisticList) {
        this.statisticList = statisticList;
    }

    public List<Map.Entry<String, Integer>> getResultList() {
        return statisticList;
    }

}
