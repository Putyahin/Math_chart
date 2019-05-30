package com.mathChart;

import java.io.Serializable;

/** This class is used for storage of information of the math
 *  chart which then will be sent in 
 *  the json format*/
public class MathResponse implements Serializable{
	
	private static final long serialVersionUID = 3828022177269384821L;
	/** Indicator of an error*/
	private int error;
	/** Link to the math chart on the Google Drive*/
	private String url;
	
	public MathResponse() {
		this.error = 0;
		this.url = "";
	}
	
	public MathResponse(int error, String url) {
		this.error = error;
		this.url = url;
	}
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
