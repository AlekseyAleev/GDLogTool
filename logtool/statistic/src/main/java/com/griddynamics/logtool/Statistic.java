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
    * This method make statistic
    * @param query - Statistic query
    *
    * @return - Vector of results
    *
     */
    public Vector makeStatistic(StatisticQuery query);
}
