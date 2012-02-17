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
        delete fieldValues['end-date'];//remove endDate
        delete fieldValues['start-date'];//remove startDate
        delete fieldValues['step'];//remove step
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

	var filtersPanel = Ext.create('Ext.form.Panel', {
        bodyPadding: 5,
        height: 600,
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
            }
        },{
            text: 'Submit',
            formBind: true, //only enabled once the form is valid
            disabled: true,
            handler: makeStatistics
        }],
    });

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
                editable: false,
                maxValue: new Date(),
                value: new Date(),
                anchor: '100%'
            }, {
                xtype: 'timefield',
                id: 'from-time-field',
                fieldLabel: 'From time',
                format: 'H:i:s',
                allowBlank: false,
                editable: false,
                value: new Date(),
                anchor: '100%'
            }, {
                xtype: 'datefield',
                id: 'to-date-field',
                fieldLabel: 'To date',
                format: 'Y-m-d',
                editable: false,
                maxValue: new Date(),
                value: new Date(),
                anchor: '100%'
            }, {
                xtype: 'timefield',
                id: 'to-time-field',
                fieldLabel: 'To time',
                format: 'H:i:s',
                editable: false,
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
            tips: {
                trackMouse: true,
                width: 124,
                height: 36,
                renderer: function(storeItem, item) {
                    this.setTitle(storeItem.get('time') + ' : ' + storeItem.get('count') + ' occurrences');
                }
            },
            xField: 'time',
            yField: 'count'
        }]
    });


    var chartPanel = Ext.create('Ext.form.Panel', {
        frame:true,
        width: '50%',
        height: 600,
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

    var graphViewer = Ext.create('Ext.form.Panel', {
            frame:true,
            bodyStyle:'padding: 0',
            width: '100%',
            height: '100%',
            layout:'column',
            items: [chartPanel]
    });
//
    var statisticsPanel = Ext.create('Ext.form.Panel', {
        frame:true,
        bodyStyle:'padding: 0',
        width: '100%',
        height: '100%',
        layout:'column',

        items: [{
		    title: 'Filters',
		    columnWidth: 3/10,
            items: [filtersPanel]
		},{
		    title: 'Statistics browser',
		    columnWidth: 7/10,
		    items: [chartPanel]
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
