package com.griddynamics.logtool;

public class ActionFactory {

    public Action create(String actionName, Storage storage, SearchServer searchServer, Consumer consumer) {
        if (actionName.equalsIgnoreCase("getTree")) {
            TreeAction ta = new TreeAction();
            ta.setStorage(storage);
            return ta;
        } else if (actionName.equalsIgnoreCase("getLog")) {
            LogAction la = new LogAction();
            la.setStorage(storage);
            return la;
        } else if (actionName.equalsIgnoreCase("deleteLog")) {
            DeleteLogAction dla = new DeleteLogAction();
            dla.setStorage(storage);
            dla.setSearchServer(searchServer);
            return dla;
        } else if (actionName.equalsIgnoreCase("deleteDirectory")) {
            DeleteDirectoryAction dda = new DeleteDirectoryAction();
            dda.setStorage(storage);
            dda.setSearchServer(searchServer);
            return dda;
        } else if (actionName.equalsIgnoreCase("alertsAction")) {
            AlertsAction aa = new AlertsAction();
            aa.setStorage(storage);
            return aa;
        } else if (actionName.equalsIgnoreCase("doSearch")) {
            GrepSearchAction gsa = new GrepSearchAction();
            gsa.setStorage(storage);
            return gsa;
        } else if (actionName.equalsIgnoreCase("doSolrSearch")) {
            SolrSearchAction ssa = new SolrSearchAction();
            ssa.setStorage(storage);
            ssa.setSearchServer(searchServer);
            return ssa;
        } else if (actionName.equalsIgnoreCase("performance")) {
            PerformanceAction pa = new PerformanceAction();
            pa.setConsumer(consumer);
            return pa;
        } else if (actionName.equalsIgnoreCase("makeStatistics")) {
            MakeStatisticsAction msa = new MakeStatisticsAction();
            msa.setStorage(storage);
            msa.setSearchServer(searchServer);
            return msa;
        } else {
            throw new RuntimeException(" was unable to find an action named '" + actionName + "'.");
        }
    }
}
