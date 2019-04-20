package com.mathChart;

import java.awt.Color;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

public class MathChart {
	public ArrayList<String> error;
	
	public MathChart() {
		error = new ArrayList<String>();
	}

	public boolean isMathChart(String function, String xStart, String xEnd) {

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
			return false;
		}
		else if (error.isEmpty())
			return true;
		else
			return false;

	}
	
	public JFreeChart drawChart(String function, String xStart, String xEnd) {
		function = function.replace("z", "+");
		function = function.replace("v", "^");
		double xstart;
		double xend;
		xstart = Double.parseDouble(xStart);
		xend = Double.parseDouble(xEnd);
		JFreeChart chart = null;
		chart = createLineChart(function, xstart, xend);
		return chart;
	}
	
	public JFreeChart createLineChart(String function, double xStart, double xEnd) {
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
