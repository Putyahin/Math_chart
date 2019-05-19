package com.mathChart;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

import com.google.api.services.drive.model.File;
import com.google.gson.Gson;

/**
 * Servlet implementation class CreateMathChart
 */
@WebServlet("/CreateMathChart")
public class CreateMathChart extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ArrayList<String> error;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateMathChart() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String function = request.getParameter("y");
		String xStart = request.getParameter("xstart");
		String xEnd = request.getParameter("xend");
		error = new ArrayList<String>();
		function = function.replace(",", ".");
		xStart = xStart.replace(",", ".");
		xEnd = xEnd.replace(",", ".");
		
		double xstart = 0.0;
		double xend = 0.0;
		try {
			xstart = Double.parseDouble(xStart);
		} catch (NullPointerException | NumberFormatException e) {
			error.add("error in x start value");
		}
		try {
			xend = Double.parseDouble(xEnd);
		} catch (NullPointerException | NumberFormatException e) {
			error.add("error in x end value");
		}
		if (xend - xstart <= 0) {
			error.add("x end must be bigger than x start");
		}
		JFreeChart chart = null;
		chart = createLineChart(function, xstart, xend);
		if (chart == null) {
			error.add("unable to create math chart");
		}
		if (error.isEmpty()) {
			MathDao db = new MathDao();
			MathHash userHash = new MathHash(function,xstart,xend);
			String url = db.getUrl(userHash.getHash());
			if (url == null) {	
		        ByteArrayOutputStream bout = new ByteArrayOutputStream();
		        BufferedOutputStream out = new BufferedOutputStream(bout);
		        ChartUtilities.writeChartAsPNG(out, chart, 600, 400);
		        byte[] byteImg = bout.toByteArray();
				File googleFile = NewFile.createGoogleFile
						(null, "image/png", String.valueOf(userHash.getHash())+".png", byteImg);
				url = googleFile.getWebContentLink();
				db.addChart(userHash.getHash(), url);
			} 
			MathResponse str = new MathResponse(0, url);
			String json = new Gson().toJson(str);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json);
			
		} else {
			MathResponse str = new MathResponse(1, error.get(0));
			String json = new Gson().toJson(str);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json);
		}
				
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
		
	}
	
	
	private JFreeChart createLineChart(String function, double xStart, double xEnd) {
		FunctionCalculator func = new FunctionCalculator(function, xStart);
		XYSeriesCollection dataSet = new XYSeriesCollection();
		XYSeries graph = new XYSeries("y");
		if (func.CheckBraces() == false) {
			error.add("error in braces");
			return null;
		}
		double oldY = func.Calculator();
		double averageY = oldY;
		if (func.CheckOp() == false) {
			error.add("error in math expression");
			return null;
		}

		int jLines = 0;
		for (double i = xStart; i < xEnd; i += (xEnd - xStart) / 600) {
			func.setX(i);
			double y = func.Calculator();
			if (!func.error.isEmpty()) {
				error.addAll(func.error);
				return null;
			}
			if (Math.abs(y) > Math.abs(oldY) * 100 && Math.abs(y) > Math.abs(averageY) * 1000) {
				dataSet.addSeries(graph);
				jLines++;
				graph = new XYSeries("y" + jLines);
				i += (xEnd - xStart) / 600;
			} else {
				graph.add(i, y);
			}
			averageY = (y + oldY) / 2;
			oldY = y;
		}
		dataSet.addSeries(graph);
		final JFreeChart chart = ChartFactory.createXYLineChart("y = " + func.getFunction(), // chart title
				"x", // x axis label
				"y", // y axis label
				dataSet, // data
				PlotOrientation.VERTICAL, false, // include legend
				true, // tooltips
				false // urls
		);
		XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setBackgroundAlpha(0.3f);
		plot.setDomainGridlinePaint(Color.gray);
		plot.setRangeGridlinePaint(Color.gray);
		ValueAxis axis = plot.getRangeAxis(); // RangeAxis
		axis.setAxisLineVisible(false);
		axis = plot.getDomainAxis(); // DomainAxis
		axis.setAxisLineVisible(false);
		plot.setAxisOffset(new RectangleInsets(1.0, 1.0, 1.0, 1.0));
		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setBaseShapesVisible(false);
		for (int i = 0; i <= jLines; i++) {
			renderer.setSeriesPaint(i, Color.RED);
		}
		plot.setRenderer(renderer);
		if (error.isEmpty())
			return chart;
		else
			return null;
	}

}
