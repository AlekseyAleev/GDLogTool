package com.griddynamics.logtool;
import org.apache.solr.common.util.NamedList;
/**
 * Created by IntelliJ IDEA.
 * User: avyshkin
 * Date: 08.02.12
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */
public class Statistic {
    private StatisticResult statisticResult;//this is result

    public Statistic(NamedList<String> list,Integer stepDiscretization,String date){
        makeStatistic(list,stepDiscretization,date);
    }

    public StatisticResult makeStatistic(NamedList<String> list,Integer stepDiscretization,String date) {
        //this method should make statistic
        return statisticResult;
    }
    public StatisticResult getStatisticResult(){
        return statisticResult;
    }

}
