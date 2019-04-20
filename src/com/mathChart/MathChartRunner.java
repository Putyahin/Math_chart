package com.mathChart;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MathChartRunner
 */
@WebServlet("/MathChartRunner")
public class MathChartRunner extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MathChartRunner() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String function = request.getParameter("y");
		String xStart = request.getParameter("xstart");
		String xEnd = request.getParameter("xend");
		function = function.replace(",", ".");
		xStart = xStart.replace(",", ".");
		xEnd = xEnd.replace(",", ".");
		StringBuilder text = new StringBuilder();
		text.append("<!DOCTYPE html>");
		text.append("<html>");
		text.append("<head>");
		text.append("<meta charset='UTF-8'>");
		text.append("<title>Math chart</title>");
		text.append("</head>");
		text.append("<body>");
		text.append("<h2>Please enter your math chart</h2>");
		text.append("<FORM ACTION=MathChartRunner METHOD=POST>");
		text.append("<P>");
		text.append("y = <INPUT TYPE=TEXT NAME=y VALUE=" + function + " SIZE=50>");
		text.append("</P>");
		text.append("<P>Enter range of x argument</P>");
		text.append("<P>");
		text.append("x start = <INPUT TYPE=TEXT NAME=xstart VALUE= " + xStart + " SIZE=20>");
		text.append("</P>");
		text.append("<P>");
		text.append("x end = <INPUT TYPE=TEXT NAME=xend VALUE= " + xEnd + " SIZE=20>");
		text.append("</P>");
		text.append("<P><INPUT TYPE=SUBMIT VALUE=Generate chart>");
		MathChart chart = new MathChart();
		if (chart.isMathChart(function, xStart, xEnd)) {
			function = function.replace("+", "z");
			function = function.replace("^", "v");
			text.append("<P>");
			text.append("<IMG SRC=\"MathChartServlet?y=" + function + "&xstart=" + xStart + "&xend=" + xEnd
					+ "\" BORDER=1 />");
			text.append("</P>");
		} else {
			text.append("<P>");
			text.append("Error: " + chart.error.get(0));
			text.append("</P>");
		}
		text.append("</FORM>");
		text.append("</body>");
		text.append("</html>");
		PrintWriter out = new PrintWriter(response.getWriter());
		response.setContentType("text/html");
		out.println(text.toString());
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
