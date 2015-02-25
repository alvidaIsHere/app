
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
<div class='container' style='align: center;'>
    <div class='row' align="center">
        <div class='col-md-12'>
            <div id="d2010">
                <svg></svg>
            </div>
            <p align="center"><strong>2010</strong></p>
            <br />
            <div id="d2011">
                <svg></svg>
            </div>
            <p align="center"><strong>2011</strong></p>
            <br />
            <div id="d2012">
                <svg></svg>
            </div>
            <p align="center"><strong>2012</strong></p>
            <br />
            <div id="d2013">
                <svg></svg>
            </div>
            <p align="center"><strong>2013</strong></p>
            <br />
            <div id="d2014">
                <svg></svg>
            </div>
            <p align="center"><strong>2014</strong></p>
        </div>
    </div>
</html>
<script type="text/javascript">
    // obtain input from json call
    $.getJSON('ViewDemo.do?datetime=<% out.print(request.getParameter("datetime")); %>&filter=yeargenderschool', function(json) {
    
        // loop through the years and store data into arrays
        // create array to store number of years
        var allYears = [{
            2010: [],
            2011: [],
            2012: [],
            2013: [],
            2014: [],
        }];
        // obtain number of years
        var years = json['breakdown'][0].breakdown;
        for(var q = 0; q < years.length; q++)   {
            // loop through all the years
            // obtain male data
            var males = json['breakdown'][0];
            //console.log(males);
            // go into year breakdown for males
            var malesYrBreakdown = males.breakdown;
            //console.log(malesYrBreakdown[0]['breakdown']);
            // populate for year 2010
            var m2010 = malesYrBreakdown[q]['breakdown'];
            var m2010data = [];
            var yr2010 = [];
            for(var x = 0; x < m2010.length; x++)   {
                m2010data.push({
                    'x': m2010[x].school,
                    'y': m2010[x].count
                });
            }
            //console.log(yr2010);
            yr2010.push({
                key: "M",
                values: m2010data
            });
            // obtain female data
            var females = json['breakdown'][1];
            //console.log(females);
            var femalesYrBreakdown = females.breakdown;
            var f2010 = femalesYrBreakdown[q]['breakdown'];
            var f2010data = [];
            for(var j = 0; j < f2010.length; j++)   {
                f2010data.push({
                    'x': f2010[j].school,
                    'y': f2010[j].count
                });
            }
            yr2010.push({
                key: "F",
                values: f2010data
            });    
            // assign all data for the year to particular year
            allYears[q] = yr2010;
            //console.log(years[q]);
        }
        console.log(allYears);
        
        //console.log(yr2010);

        nv.addGraph({
            generate: function() {
                var width = nv.utils.windowSize().width - 240,
                        height = nv.utils.windowSize().height - 340;

                var chart = nv.models.multiBarChart()
                        .width(width)
                        .height(height)
                        .stacked(false)
                        .showControls(false)

                var svg = d3.select('#d2010 svg')
                        .attr('width', width)
                        .attr('height', height)
                        .datum(allYears[0]);
                var svg2 = d3.select('#d2011 svg')
                        .attr('width', width)
                        .attr('height', height)
                        .datum(allYears[1]);

                var svg3 = d3.select('#d2012 svg')
                        .attr('width', width)
                        .attr('height', height)
                        .datum(allYears[2]);

                var svg4 = d3.select('#d2013 svg')
                        .attr('width', width)
                        .attr('height', height)
                        .datum(allYears[3]);

                var svg5 = d3.select('#d2014 svg')
                        .attr('width', width)
                        .attr('height', height)
                        .datum(allYears[4]);


                svg.transition().duration(500).call(chart);
                svg2.transition().duration(500).call(chart);
                svg3.transition().duration(500).call(chart);
                svg4.transition().duration(500).call(chart);
                svg5.transition().duration(500).call(chart);

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

                    d3.select('#d2010 svg')
                            .attr('width', width)
                            .attr('height', height)
                    d3.select('#d2011 svg')
                            .attr('width', width)
                            .attr('height', height)
                    d3.select('#d2012 svg')
                            .attr('width', width)
                            .attr('height', height)
                    d3.select('#d2013 svg')
                            .attr('width', width)
                            .attr('height', height)
                    d3.select('#d2014 svg')
                            .attr('width', width)
                            .attr('height', height)
                    
                    transition().duration(500)
                            .call(graph);

                };
            }
        });
    });


</script>
