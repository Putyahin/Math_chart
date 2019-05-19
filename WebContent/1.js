function genChart() {
    var out = document.getElementById('chartOut');
    var y = document.getElementById('y').value;
    var xstart = document.getElementById('xstart').value;
    var xend = document.getElementById('xend').value;
    
    var error;
    var url;
    
    out.innerHTML  = '';
    out.innerHTML += 'y = ' + y + '<br>';
    out.innerHTML += 'x start = ' + xstart + '<br>';
    out.innerHTML += 'x end = ' + xend + '<br>';
    
    $.post('ServletTest', {'y': y, 'xstart' : xstart , 'xend' : xend},
        function(servletData) {
            $.getJSON(servletData, function(jsonData) {
                    error = jsonData.error;
                    url = jsonData.url;
                });
        });
    out.innerHTML += 'error = ' + error + '<br>';
    out.innerHTML += 'url = ' + url + '<br>';
    
    out.innerHTML += '<hr>';
}