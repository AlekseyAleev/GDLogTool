package com.griddynamics.logtool;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: avyshkin
 * Date: 14.02.12
 * Time: 18:20
 * To change this template use File | Settings | File Templates.
 */
public class StatisticAction extends Action{
    public void perform(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletOutputStream sos = response.getOutputStream();
        Statistic statistic = new StatisticImpl(searchServer);
        String query = request.getParameter("query");
        Integer step = Integer.parseInt(request.getParameter("step"));
        String startDate  = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        StatisticQuery statisticQuery = new StatisticQuery(makeStatQuery(query),step,startDate,endDate);
        StatisticResult result = statistic.makeStatistic(statisticQuery);

    }
    public Map<String,List<String>> makeStatQuery(String query){
        return new HashMap<String,List<String>>();
    }

}
