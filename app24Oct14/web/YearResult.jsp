
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="nvd3/js/d3.v3.js"></script>
        <script src="nvd3/js/nv.d3.js"></script>
        <script src="nvd3/js/tooltip.js"></script>
        <script src="nvd3/js/legend.js"></script>
        <script src="nvd3/js/axis.js"></script>
        <script src="nvd3/js/multiBar.js"></script>
        <script src="nvd3/js/utils.js"></script>
        <script src="nvd3/js/stream_layers.js"></script>
    </head>
    <div id="test1" align="center">
        <svg></svg>                     
    </div>                    
    <script type="text/javascript">
        // obtain input from json call
        $.getJSON('ViewDemo.do?datetime=<% out.print(request.getParameter("datetime"));%>&filter=year', function(json) {
            for (var key in json) {
                if (json.hasOwnProperty(key)) {
                    var item = json[key];
                    console.log(item);
                    var data1 = [];
                    for (var i = 0; i < item.length; i++) {
                        data1.push({
                            key: item[i].year,
                            values: [{'x': item[i].year, 'y': item[i].count}]
                        });
                    }
                }
            }
            nv.addGraph({
                generate: function() {
                    var width = nv.utils.windowSize().width - 240,
                            height = nv.utils.windowSize().height - 340;

                    var chart = nv.models.multiBarChart()
                            .width(width)
                            .height(height)
                            .stacked(false)
                            .showControls(false)

                    var svg = d3.select('#test1 svg')
                            .attr('width', width)
                            .attr('height', height)
                            .datum(data1);

                    svg.transition().duration(500).call(chart);

                    return chart;
                },
                callback: function(graph) {

                    window.onresize = function() {
                        var width = nv.utils.windowSize().width - 240,
                                height = nv.utils.windowSize().height - 340,
                                margin = graph.margin();


                        if (width < margin.left + margin.right + 20)
                            width = margin.left + margin.right + 20;

                        if (height < margin.top + margin.bottom + 20)
                            height = margin.top + margin.bottom + 20;


                        graph
                                .width(width)
                                .height(height);

                        d3.select('#test1 svg')
                                .attr('width', width)
                                .attr('height', height)
                        transition().duration(500)
                                .call(graph);

                    };
                }
            });
        });
    </script>
