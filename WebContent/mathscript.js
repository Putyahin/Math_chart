function genChart() {
	var out = document.getElementById('chartOut');
	var y = document.getElementById('y').value;
	var xstart = document.getElementById('xstart').value;
	var xend = document.getElementById('xend').value;

	var error = '';
	var url = '';

	out.innerHTML = '';
		
	$.getJSON('CreateMathChart', {
		'y' : y,
		'xstart' : xstart,
		'xend' : xend
	},function(servletData) {
		error = servletData.error;
		url = servletData.url;
		if (error == 0) {
			out.innerHTML += '<img src="' + url + '">';
		}
		else {
			out.innerHTML += 'Error: ' + url;
		}
		out.innerHTML += '<hr>';
				
    });
	
	
}