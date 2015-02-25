
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link href="bootstrap/datetimepicker/css/bootstrap-datetimepicker.css" rel="stylesheet">
        <script src="bootstrap/js/jquery-2.1.1.min.js"></script>
        <script src="bootstrap/datetimepicker/js/moment.js"></script> 
        <script src="bootstrap/datetimepicker/js/bootstrap-datetimepicker.js"></script>  
        <script src="bootstrap/js/bootstrap.min.js"></script>
        <script src="bootstrap/js/collapse.js"></script>
        <script src="bootstrap/js/transition.js"></script>     
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
</html>
<script type="text/javascript">
    // obtain input from json call
    $.getJSON('ViewDemo.do?datetime=<% out.print(request.getParameter("datetime"));%>&filter=yeargender', function(json) {

        for (var key in json) {
            if (json.hasOwnProperty(key)) {
                var item = json['breakdown'];
                // array to store data
                var data1 = [];
                for (var obj in item) {
                    var val = [];
                    for (var j = 0; j < item[obj]['breakdown'].length; j++) {
                        val.push({
                            x: item[obj].year,
                            y: item[obj]['breakdown'][0].count
                        });
                    }
                    data1.push({
                        key: "M",
                        values: val
                    });
                    val = [];
                    for (var j = 0; j < item[obj]['breakdown'].length; j++) {
                        val.push({
                            x: item[obj].year,
                            y: item[obj]['breakdown'][1].count
                        });
                        //out.print("{'x':" + item[obj].year + ", 'y':" + item[obj['breakdown'][0].count] + "} ,");
                    }
                    data1.push({
                        key: "F",
                        values: val
                    });
                }
                console.log(data1);
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
