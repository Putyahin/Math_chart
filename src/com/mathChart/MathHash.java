package com.mathChart;

/**
 * Used to create unique id of user's math function
 * @param function - user's math function
 * @param xstart - start of x interval
 * @param xend - end of x interval
 *
 */
public class MathHash {
	
	/** function field*/
	private String function;
	
	/** xstart field*/
	private double xstart;
	
	/** xend field*/
	private double xend;
	
	/** hash filed*/
	private int hash;

//	public MathHash() {
//
//	}

	/** Constructor for MathHash
	 * @param function - user's math function
	 * @param xstart - start of x interval
	 * @param xend - end of x interval
	 * */
	public MathHash(String function, double xstart, double xend) {
		this.function = function;
		this.xstart = xstart;
		this.xend = xend;
		hash = hashCode();
	}

	/** Getter for field function
	 * @return value of field function*/
	public String getFunction() {
		return function;
	}

	/** Setter for field function*/
	public void setFunction(String function) {
		this.function = function;
	}

	/** Getter for field xstart*/
	public double getXstart() {
		return xstart;
	}

	/** Setter for field xstart*/
	public void setXstart(double xstart) {
		this.xstart = xstart;
	}

	/** Getter for field xend*/
	public double getXend() {
		return xend;
	}

	/** Setter for field xend*/
	public void setXend(double xend) {
		this.xend = xend;
	}

	/** This method generates unique value for combination of three fields: 
	 * function, xstart, xend. This value is used as a key in the database*/
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
