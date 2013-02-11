package com.venky.swf.plugins.calendar.db.model;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class DOW {
	static final Map<String,Integer> iDows = new HashMap<String, Integer>();
	static final Map<Integer,String> sDows = new HashMap<Integer, String>();
	
	static {
		StringTokenizer tok = new StringTokenizer(WorkDay.DOWS,",");
		int i = 1; 
		while (tok.hasMoreElements()){
			String day = tok.nextToken();
			iDows.put(day,i);
			sDows.put(i, day);
			i++;
		}
	}
	public static int getDOWNumber(String dow){
		Integer iDow = iDows.get(dow);
		if (iDow == null){
			throw new IllegalArgumentException(dow + " is not a valid day of the week");
		}
		return iDow;
	}
	
	public static String getDOWString(int iDow){
		String sDow = sDows.get(iDow);
		if (sDow == null){
			throw new IllegalArgumentException(iDow + " is not a valid day of the week");
		}
		return sDow;
	}
}
