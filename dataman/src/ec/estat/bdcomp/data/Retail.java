package ec.estat.bdcomp.data;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Vector;

import ec.estat.bdcomp.data.Indicator.Periodicity;
import ec.estat.bdcomp.data.Indicator.Type;

public class Retail extends Indicator {

	public enum Countries {a, b};	
	public Retail(boolean sa) {
		this.p = Periodicity.MONTHLY;
		this.t = Type.INDEX;
		this.formatter = new SimpleDateFormat("yyyy-MM");
		this.sa = sa;
	}
	
	public String getFilename() {
		return (this.sa ? "retail_adj":"retail_gross");	
	}
	
	public Vector<String> getCountryAbbreviations() {
		String[] s = new String[]{"AT", "BE", "BG", "HR", "CY", "CZ", "DK", "EE", "FI", "FR", "DE", "El", "HU", "IE", "IT", "LV", "LT", "LU", "MT", "NL", "PL", "PT", "RO", "SK", "SI", "SP", "SE", "UK", "EA19", "EU28"};
		return (new Vector<String>(Arrays.asList(s)));
	}
	public boolean equals(Indicator j) {		
		if (!(super.equals(j))) { return false;};
		if (! (j instanceof Retail) || this.sa != j.isSa() || this.p != j.getPeriodicity() || this.t != j.getType()) {
			return false;
		} else {
			return true;
		}	
	}
}
