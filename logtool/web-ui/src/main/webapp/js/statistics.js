Ext.require([
    'Ext.form.*',
    'Ext.layout.container.Column',
    'Ext.tab.Panel'
]);

Ext.onReady(function(){

    var loc = location.href;
    loc = loc.substring(0, loc.lastIndexOf('/'));

    Ext.QuickTips.init();

	var filtersGrid = Ext.create('Ext.form.Panel', {
	    frame:true,
        bodyStyle:'padding: 0',
        width: '100%',
        height: '100%',
        layout:'column',
    });

    var queryField = Ext.create('Ext.form.field.Text', {
            id: 'query-field',
            emptyText: 'Write solr query...',
            size: 200
        });

    var graphViewer = Ext.create('Ext.form.Panel', {
            frame:true,
            bodyStyle:'padding: 0',
            width: '100%',
            height: '100%',
            layout:'column',
        });

    var statisticsPanel = Ext.create('Ext.form.Panel', {
        frame:true,
        bodyStyle:'padding: 0',
        width: '100%',
        height: '100%',
        layout:'column',

        items: [{
		    title: 'Filters',
		    columnWidth: 3/10,
            items: filtersGrid
		},{
		    title: 'Statistics viewboard',
		    columnWidth: 7/10,
		    items: [queryField, graphViewer]
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
