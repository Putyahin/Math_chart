package com.mathChart;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jfree.chart.ChartUtilities;

/**
 * Servlet implementation class MathChartServlet
 */
@WebServlet("/MathChartServlet")
public class MathChartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MathChartServlet() {
		super();
		// TODO Auto-generated constructor stub
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
		function = function.replace("z", "+");
		function = function.replace("v", "^");
		xStart = xStart.replace(",", ".");
		xEnd = xEnd.replace(",", ".");
		MathChart chart = new MathChart();
		try (OutputStream out = response.getOutputStream()) {
			response.setContentType("image/png");
			ChartUtilities.writeChartAsPNG(out, chart.drawChart(function, xStart, xEnd), 600, 400);
			out.close();
		}

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
