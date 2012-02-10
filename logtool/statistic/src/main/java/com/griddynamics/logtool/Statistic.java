package com.griddynamics.logtool;
import org.apache.solr.common.util.NamedList;

import java.util.LinkedHashMap;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: avyshkin
 * Date: 08.02.12
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */
public interface Statistic {
    /*
    * This method make solor requests and calculate statistic
    * @param map - linked map of field names and values
    * @param stepDiscretization - step of date discretization
    * @param startDate - date to start
    * @param endDate - date to end
    *
    * @return result of statistic
    */
    public Vector makeStatistic(StatisticQuery query);
    public Vector getStatisticResult();
}
