package ec.estat.bdcomp.data;

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
		
	public Periodicity p;
	public Type t;
	
}
