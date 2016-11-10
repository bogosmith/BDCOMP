package ec.estat.bdcomp.data;

import java.util.Vector;
import java.text.SimpleDateFormat;

public abstract class Indicator {
	public enum Periodicity {
		MONTHLY,
		QUARTERLY,
		ANNUAL
	}
	public enum Type {
		INDEX,
		GROWTHRATE,
		COUNT
	}
	//indicates whether the series is seasonally adjusted or not
	protected boolean sa;
	//the filename that the crawler uses to save this type of indicator
	public abstract String getFilename();
	public abstract Vector<String> getCountryAbbreviations();
	public abstract String toString();
	
	public boolean equals(Indicator j) { return (this.getClass().isInstance(j));};
		
	protected Periodicity p;
	protected Type t;
	protected SimpleDateFormat formatter;
	
	public boolean isSa() {
		return sa;
	}

	public Periodicity getPeriodicity() {
		return p;
	}

	public Type getType() {
		return t;		
	}
	
	public SimpleDateFormat getFormatter() {
		return formatter;
	}
	
	
}
