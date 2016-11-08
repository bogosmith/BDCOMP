package ec.estat.bdcomp.data;

import java.util.Vector;

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
	public abstract Vector<String> getCountryAbbreviations();
		
	protected Periodicity p;
	protected Type t;
	public boolean isSa() {
		return sa;
	}

	public Periodicity getPeriodicity() {
		return p;
	}

	public Type getType() {
		return t;
	}
	
}
