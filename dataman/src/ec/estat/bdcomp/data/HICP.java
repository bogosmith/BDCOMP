package ec.estat.bdcomp.data;

import java.util.Arrays;
import java.util.Vector;

public class HICP extends Indicator {

	public enum Countries {a, b};
	public HICP(boolean sa) {
		this.p = Periodicity.MONTHLY;
		this.t = Type.INDEX;
		this.sa = sa;
	}
	
	public Vector<String> getCountryAbbreviations() {
		String[] s = new String[]{"AT", "BE", "BG", "HR", "CY", "CZ", "DK", "EE", "FI", "FR", "DE", "El", "HU", "IE", "IT", "LV", "LT", "LU", "MT", "NL", "PL", "PT", "RO", "SK", "SI", "SP", "SE", "UK", "EA", "EU"};
		return (new Vector<String>(Arrays.asList(s)));
	}
}
