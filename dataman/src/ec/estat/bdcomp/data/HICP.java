package ec.estat.bdcomp.data;

import java.util.Arrays;
import java.util.Vector;
import java.text.SimpleDateFormat;

public class HICP extends Indicator {

	//public enum Countries {a, b};
	public HICP(boolean sa) {
		this.p = Periodicity.MONTHLY;
		this.t = Type.INDEX;
		this.formatter = new SimpleDateFormat("yyyy-MM");
		this.sa = sa;
	}
	
	public String getFilename() {
		return "hicp";	
	}
	
//	public String toString() {
//		return "HICP " + this.p + " " + (this.sa?" seasonally adjusted ":"non seasonally adjusted ") + " " + this.t; 
//	}
	
	public Vector<String> getCountryAbbreviations() {
		String[] s = new String[]{"AT", "BE", "BG", "HR", "CY", "CZ", "DK", "EE", "FI", "FR", "DE", "El", "HU", "IE", "IT", "LV", "LT", "LU", "MT", "NL", "PL", "PT", "RO", "SK", "SI", "SP", "SE", "UK", "EA", "EU"};
		return (new Vector<String>(Arrays.asList(s)));
	}
	public boolean equals(Indicator j) {		
		if (!(super.equals(j))) { return false;};
		if (! (j instanceof HICP) || this.sa != j.isSa() || this.p != j.getPeriodicity() || this.t != j.getType()) {
			return false;
		} else {
			return true;
		}		
	}
}
