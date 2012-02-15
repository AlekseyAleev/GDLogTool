package com.griddynamics.logtool;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: avyshkin
 * Date: 14.02.12
 * Time: 18:20
 * To change this template use File | Settings | File Templates.
 */
public class MakeStatisticsAction extends Action{
    public void perform(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletOutputStream sos = response.getOutputStream();
        Statistic statistic = new StatisticImpl(searchServer);

        String query = request.getParameter("query");
        Integer step = Integer.parseInt(request.getParameter("step"));
        String startDate  = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        StatisticQuery statisticQuery = new StatisticQuery(makeStatQuery(query),step,startDate,endDate);
        StatisticResult result = statistic.makeStatistic(statisticQuery);
        sos.print(getJsonFromList(result.getStatistic()));
    }
    public Map<String,List<String>> makeStatQuery(String query){
        String[] splitStrings = query.split("\\s");
        HashMap<String,List<String>> map  = new HashMap<String,List<String>>();
        for (String pair: splitStrings){
            String[]fields = pair.split(":");
            if(map.containsKey(fields[0])){
                map.get(fields[0]).add(fields[1]);
            } else {
                LinkedList<String> list = new LinkedList<String>();
                list.add(fields[1]);
                map.put(fields[0],list);
            }
        }
        return map;
    }
    public String getJsonFromList(List<Integer> list){
        StringBuilder stringBuilder = new StringBuilder("{\"statistic\":[");
        for(Integer number:list){
            stringBuilder.append(number.toString()).append(", ");
        }
        int index = stringBuilder.lastIndexOf(", ");
        if (index > -1) {
            stringBuilder.replace(index, index + 2, "");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }

}
