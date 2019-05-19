package com.mathChart;

public class MathHash {
	private String function;
	private double xstart;
	private double xend;
	private int hash;

//	public MathHash() {
//
//	}

	public MathHash(String function, double xstart, double xend) {
		this.function = function;
		this.xstart = xstart;
		this.xend = xend;
		hash = hashCode();
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public double getXstart() {
		return xstart;
	}

	public void setXstart(double xstart) {
		this.xstart = xstart;
	}

	public double getXend() {
		return xend;
	}

	public void setXend(double xend) {
		this.xend = xend;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((function == null) ? 0 : function.hashCode());
		long temp;
		temp = Double.doubleToLongBits(xend);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(xstart);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MathHash other = (MathHash) obj;
		if (function == null) {
			if (other.function != null)
				return false;
		} else if (!function.equals(other.function))
			return false;
		if (Double.doubleToLongBits(xend) != Double.doubleToLongBits(other.xend))
			return false;
		if (Double.doubleToLongBits(xstart) != Double.doubleToLongBits(other.xstart))
			return false;
		return true;
	}
	
	public int getHash() {
		return hash;
	}

}
