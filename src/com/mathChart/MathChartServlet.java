package com.mathChart;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
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
import org.jfree.ui.RectangleInsets;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

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
		// TODO Auto-generated method stub
		String function = request.getParameter("y");
		String xStart = request.getParameter("xstart");
		String xEnd = request.getParameter("xend");
		function = function.replace(",", ".");
		function = function.replace("z", "+");
		function = function.replace("v", "^");
		xStart = xStart.replace(",", ".");
		xEnd = xEnd.replace(",", ".");
		double xstart = 0.0;
		double xend = 0.0;
		try {
			xstart = Double.parseDouble(xStart);
			xend = Double.parseDouble(xEnd);

		} catch (Exception e) {
			System.err.println(e.toString());
		}
		OutputStream out = response.getOutputStream();
		try {
			JFreeChart chart = null;
			chart = createLineChart(function, xstart, xend);
			if (chart != null) {
				response.setContentType("image/png");

				ChartUtilities.writeChartAsPNG(out, chart, 600, 400);
			}
		} catch (Exception e) {
			System.err.println(e.toString());
		} finally {
			out.close();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private JFreeChart createLineChart(String function, double xStart, double xEnd) {
		FunctionCalculator func = new FunctionCalculator(function, xStart);
		XYSeriesCollection dataSet = new XYSeriesCollection();
		XYSeries graph = new XYSeries("y");
		double averageY = func.Calculator();
		int j = 0;
		for (double i = xStart; i < xEnd; i += (xEnd - xStart) / 600) {
			func.setX(i);
			double y = func.Calculator();
			if (Math.abs(y) > Math.abs(averageY) * 10000) {
				dataSet.addSeries(graph);
				j++;
				graph = new XYSeries("y" + j);
				i += (xEnd - xStart) / 600;
			} else {
				averageY = (y + averageY) / 2;
				graph.add(i, y);
			}
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

		// Р¤РѕРЅ РіСЂР°С„РёРєР°
		plot.setBackgroundPaint(Color.lightGray);
		plot.setBackgroundAlpha(0.3f);

		// Р¦РІРµС‚ СЃРµС‚РєРё РЅР°
		// РґРёР°РіСЂР°Р�?Р�?Рµ
		plot.setDomainGridlinePaint(Color.gray);
		plot.setRangeGridlinePaint(Color.gray);

		// РЈРґР°Р»СЏРµР�? РёР· РґРёР°РіСЂР°Р�?Р�?С‹
		// РѕСЃРµРІС‹Рµ Р»РёРЅРёРё
		ValueAxis axis = plot.getRangeAxis(); // RangeAxis
		axis.setAxisLineVisible(false);
		axis = plot.getDomainAxis(); // DomainAxis
		axis.setAxisLineVisible(false);
		// РћРїСЂРµРґРµР»РµРЅРёРµ РѕС‚СЃС‚СѓРїР°
		// Р�?РµС‚РѕРє РґРµР»РµРЅРёР№
		plot.setAxisOffset(new RectangleInsets(1.0, 1.0, 1.0, 1.0));

		// Р�СЃРєР»СЋС‡Р°РµР�?
		// РїСЂРµРґСЃС‚Р°РІР»РµРЅРёРµ Р»РёРЅРёР№
		// 1-РіРѕ
		// РіСЂР°С„РёРєР°
		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setBaseShapesVisible(false);
		for (int i = 0; i <= j; i++) {
			renderer.setSeriesPaint(i, Color.RED);
		}
		// renderer.setSeriesShapesVisible(0, false);

//        renderer.setSeriesLinesVisible (0, false);
//      renderer.setSeriesShapesVisible(1, false);
		plot.setRenderer(renderer);

//      change the auto tick unit selection to integer units only
//      final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
//      rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		return chart;
	}

}
