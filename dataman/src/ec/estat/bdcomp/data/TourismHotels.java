package ec.estat.bdcomp.data;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Vector;

import ec.estat.bdcomp.data.Indicator.Periodicity;
import ec.estat.bdcomp.data.Indicator.Type;

public class TourismHotels extends Indicator {
	
	public TourismHotels(boolean sa) {
		this.p = Periodicity.MONTHLY;
		this.t = Type.COUNT;
		this.formatter = new SimpleDateFormat("yyyy-MM");
		this.sa = sa;
	}
	
	public String getFilename() {
		return  ("tour_hotel");	
	}
	
	public Vector<String> getCountryAbbreviations() {
		String[] s = new String[]{"AT", "BE", "BG", "HR", "CY", "CZ", "DK", "EE", "FI", "FR", "DE", "El", "HU", "IE", "IT", "LV", "LT", "LU", "MT", "NL", "PL", "PT", "RO", "SK", "SI", "SP", "SE", "UK", "EA19", "EA", "EU28"};
		return (new Vector<String>(Arrays.asList(s)));
	}
	public boolean equals(Indicator j) {		
		if (!(super.equals(j))) { return false;};
		if (! (j instanceof TourismHotels) || this.sa != j.isSa() || this.p != j.getPeriodicity() || this.t != j.getType()) {
			return false;
		} else {
			return true;
		}	
	}
}
