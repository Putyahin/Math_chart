package com.mathChart;


public class TestRun {
	public static void main(String args[] ) {
		
		MathDao db = new MathDao();
		
		db.getAllCharts();
		
		MathHash userHash = new MathHash("x*cos(3*x+1)",-5.0,8.0);
		System.out.println(userHash.getHash());
		
		
	}
}
