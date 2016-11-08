package ec.estat.bdcomp.data;

import java.util.Arrays;
import java.util.Vector;

public class Retail extends Indicator {

	public enum Countries {a, b};
	public Retail() {
		this.p = Periodicity.MONTHLY;
		this.t = Type.INDEX;		
	}
	
	public Vector<String> getCountryAbbreviations() {
		String[] s = new String[]{"AT", "BE", "BG", "HR", "CY", "CZ", "DK", "EE", "FI", "FR", "DE", "El", "HU", "IE", "IT", "LV", "LT", "LU", "MT", "NL", "PL", "PT", "RO", "SK", "SI", "SP", "SE", "UK", "EA19", "EU28"};
		return (new Vector<String>(Arrays.asList(s)));
	}
}
