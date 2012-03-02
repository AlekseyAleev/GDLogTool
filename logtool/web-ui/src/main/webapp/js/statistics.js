Ext.require([
    'Ext.form.*',
    'Ext.layout.container.Column',
    'Ext.tab.Panel'
]);

Ext.onReady(function(){

    var loc = location.href;
    loc = loc.substring(0, loc.lastIndexOf('/'));

    var statisticQuery = "";
    var statisticResult = [];
    var startDate;
    var endDate;
    var stepOfDiscretization;

    Ext.QuickTips.init();

    function submitCustomDate() {
        var fromDate = Ext.getCmp('from-date-field');
        var fromTime = Ext.getCmp('from-time-field');
        var toDate = Ext.getCmp('to-date-field');
        var toTime = Ext.getCmp('to-time-field');
        if(fromDate.validate() || fromTime.validate()) {
            if(!fromDate.validate()) {
                fromDate.setValue(toDate.getValue());
            } else if (!fromTime.validate()){
                fromTime.setValue(toTime.getValue());
            }
            var fromValue = fromDate.getSubmitValue() + 'T' + fromTime.getSubmitValue() + 'Z';
            var toValue = toDate.getSubmitValue() + 'T' + toTime.getSubmitValue() + 'Z';
            filtersPanel.getForm().findField('start-date').setValue(fromValue);
            filtersPanel.getForm().findField('end-date').setValue(toValue);
            return true;
        } else {
            return false;
        }
    }

    function makeStatistics() {
        startDate = filtersPanel.getForm().findField('start-date').getValue();
        endDate = filtersPanel.getForm().findField('end-date').getValue();
        stepOfDiscretization = filtersPanel.getForm().findField('step').getValue();
        composeQuery();

        if (statisticQuery != "" && stepOfDiscretization != 0) {
            Ext.Ajax.request({
                url : loc + '/logtool' ,
                params : {
                    action: 'makeStatistics',
                    query: statisticQuery,
                    step: stepOfDiscretization,
                    startDate: startDate,
                    endDate: endDate
                },
                method: 'GET',
                success: function (result, request) {
                    statisticResult = Ext.decode(result.responseText);
                    store.loadData(generateData());
                    pieStore.loadData(generateDataToPieChart());
                },
                failure: function (result, request) {
                    Ext.MessageBox.alert('Failed', result.responseText);
                }
            });
        }
    }

    function composeQuery() {
        statisticQuery = "";
        var fieldValues = filtersPanel.getForm().getValues();

        //delete fields below because they will be passed as separate parameters
        delete fieldValues['end-date'];
        delete fieldValues['start-date'];
        delete fieldValues['step'];
        for (fieldValue in fieldValues) {
            if (fieldValues[fieldValue] != "") {
                statisticQuery += fieldValue + ":" + fieldValues[fieldValue] + "|";
            }
        }
        statisticQuery = statisticQuery.substring(0, statisticQuery.length - 1);
        return statisticQuery;
    }

    function generateData() {
        var halfLength = statisticResult.length / 2;
        var data = [], i, strTime;
        for (i = 0; i < halfLength; ++i) {
            strTime = statisticResult[i + halfLength];
            strTime = strTime.substring(0, strTime.length - 1).replace("T"," ");
            data.push({
                time: strTime,
                count: parseInt(statisticResult[i])
            });
        }
        return data;
    }

    function generateDataToPieChart() {
        var halfLength = statisticResult.length / 2;
        var data = [], i, strTime;
        for (i = 0; i < halfLength; ++i) {
            var val = parseInt(statisticResult[i]);
            if (val != 0) {
                strTime = statisticResult[i + halfLength];
                strTime = strTime.substring(0, strTime.length - 1).replace("T"," ");
                data.push({
                    time: strTime,
                    count: val
                });
            }
        }
        return data;
    }


	var filtersPanel = Ext.create('Ext.form.Panel', {
        bodyPadding: 20,
        height: 320,
        layout: 'anchor',
        defaults: {
            anchor: '100%'
        },

        defaultType: 'textfield',
        items: [{
            fieldLabel: 'Content',
            name: 'content',
            allowBlank: true
        },{
            fieldLabel: 'Tag',
            name: 'tags',
            allowBlank: true
        },{
            fieldLabel: 'Level',
            name: 'level',
            allowBlank: true
        },{
            fieldLabel: 'Host',
            name: 'host',
            allowBlank: true
        },{
            fieldLabel: 'Application',
            name: 'application',
            allowBlank: true
        },{
            fieldLabel: 'Instance',
            name: 'instance',
            allowBlank: true
        },{
            fieldLabel: 'Step of discretization',
            name: 'step',
            allowBlank: false
        },{
            fieldLabel: 'Start date',
            name: 'start-date',
            allowBlank: false
        }
        ,{
            fieldLabel: 'End date',
            name: 'end-date',
            allowBlank: false
        }],

        buttons: [
        {
            text: 'Open custom date dialog',
            handler: function() {
                customDateWindow.show();
            }
        },{
            text: 'Reset',
            handler: function() {
                this.up('form').getForm().reset();
                statisticResult = [];
                store.loadData(generateData());
                pieStore.loadData(generateDataToPieChart());
            }
        },{
            text: 'Submit',
            formBind: true, //only enabled once the form is valid
            disabled: true,
            handler: makeStatistics
        }],
    });

    //Be careful! Due to timeformatters there are different timezones and incorrect time representation.
    var customDateWindow = Ext.create('Ext.window.Window', {
        title: 'Custom date',
        layout: 'anchor',
        bodyPadding: '5 5 5 5',
        closable: false,
        modal: true,
        items: [
            {
                xtype: 'datefield',
                id: 'from-date-field',
                fieldLabel: 'From date',
                format: 'Y-m-d',
                allowBlank: false,
                editable: true,
                maxValue: new Date(),
                value: new Date(),
                anchor: '100%'
            }, {
                xtype: 'timefield',
                id: 'from-time-field',
                fieldLabel: 'From time',
                format: 'H:i:s',
                allowBlank: false,
                editable: true,
                value: new Date(),
                anchor: '100%'
            }, {
                xtype: 'datefield',
                id: 'to-date-field',
                fieldLabel: 'To date',
                format: 'Y-m-d',
                editable: true,
                maxValue: new Date(),
                value: new Date(),
                anchor: '100%'
            }, {
                xtype: 'timefield',
                id: 'to-time-field',
                fieldLabel: 'To time',
                format: 'H:i:s',
                editable: true,
                value: new Date(),
                anchor: '100%'
            }
        ],
        buttons: [
            {
                text: 'Ok',
                handler: function() {
                    var submitState = submitCustomDate();
                    if(submitState) {
                        customDateWindow.hide();
                    }
                }
            },
            {
                text: 'Cancel',
                handler: function() {
                    Ext.getCmp('from-date-field').reset();
                    Ext.getCmp('from-time-field').reset();
                    customDateWindow.hide();
                }
            }
        ]
    })

    var store = Ext.create('Ext.data.JsonStore', {
        fields:['time', 'count'],
        data: generateData()
    });

    var pieStore = Ext.create('Ext.data.JsonStore', {
        fields:['time', 'count'],
        data: generateDataToPieChart()
    });

    var pieChart = Ext.create('Ext.chart.Chart', {
        animate: true,
        shadow: true,
        store: pieStore,
        style: 'background:#fff',
        shadow: true,
        legend: {
            position: 'right'
        },
        series: [{
            type: 'pie',
            showInLegend: true,
            field: ['count'],
            label: {
                field: 'time',
                display: 'rotate',
                contrast: true,
                font: '18px Arial'
            },
            highlight: {
                segment: {
                    margin: 20
                }
            }
        }]
    });

    var columnChart = Ext.create('Ext.chart.Chart', {
        animate: true,
        shadow: true,
        store: store,
        style: 'background:#fff',
        axes: [{
            type: 'Category',
            position: 'bottom',
            fields: ['time'],
            title: 'Timestamp',

        }, {
            type: 'Numeric',
            position: 'left',
            fields: ['count'],
            title: 'Number of occurrences',
            grid: true,
            minimum: 0
        }],
        series: [{
            type: 'column',
            axis: 'left',
            highlight: true,
            style: {
                fill:'rgb(65,105,225)'
            },
            tips: {
                trackMouse: true,
                width: 124,
                height: 36,
                renderer: function(storeItem, item) {
                    this.setTitle(storeItem.get('time') + '  ' + storeItem.get('count') + ' occurrences');
                }
            },
            xField: 'time',
            yField: 'count'
        }]
    });

    var pieChartPanel = Ext.create('Ext.form.Panel', {
        frame:true,
        columnWidth: 1/2,
        height: '80%',
        bodyStyle:'padding: 0',
        title: 'ddd Pie Chart',
        layout: 'fit',
        items: [pieChart]
    });

    var columnChartPanel = Ext.create('Ext.form.Panel', {
        frame:true,
        columnWidth: 1/2,
        height: '80%',
        bodyStyle:'padding: 0',
        title: 'Column Chart',
        layout: 'fit',
        items: [columnChart]
    });

    var queryField = Ext.create('Ext.form.field.Text', {
            id: 'query-field',
            emptyText: 'Type solr query here...',
            size: 80
    });

    var statisticsPanel = Ext.create('Ext.form.Panel', {
        frame:true,
        bodyStyle:'padding: 0',
        width: '100%',
        height: '100%',
        layout:'column',

        items: [{
		    title: 'Filters',
		    columnWidth: 2/10,
		    height: '100%',
            items: [filtersPanel]
		},{
		    title: 'Statistics browser',
		    columnWidth: 8/10,
		    height: '100%',
		    layout:'column',
		    items: [columnChartPanel, pieChartPanel]
		}],
		dockedItems : [{
			xtype : 'toolbar',
			items : [
			'<b style = "font-size: 16; color: black">GDLogTool statistics</b>',
			'->',
				{
					icon : 'extjs/resources/themes/images/default/shared/left-btn.gif',
					text: '<b style = "font-size: 12; color: black">Logs</b>',
					handler : function() {
				        window.location = loc + "/";
				    }
				}
			]
		}]
    });

    statisticsPanel.render(document.getElementById("content"));
});
