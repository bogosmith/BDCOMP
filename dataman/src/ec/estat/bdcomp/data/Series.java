package ec.estat.bdcomp.data;

import ec.estat.bdcomp.BDCOMPException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Series {
	
	public enum Country {
		AT, BE, BG, HR, CY, CZ, DK, EE, FI, FR, DE, El, HU, IE, IT, LV, LT, LU, MT, NL, PL, PT, RO, SK, SI, SP, SE, UK, EA, EU		
	} 
	
	private Indicator i;
	private Date firstPeriod;
	private Date lastPeriod;
	private int datapoints;
	private SimpleDateFormat formatter;
	private Vector<Integer> series;
	private Country c;
		
	public Country getCountry() {
		return c;
	}

	public void setCountry(Country c) {
		this.c = c;
	}

	public Vector<Integer> getSeries() {		
		return series;
	}

	public void setSeries(Vector<Integer> series) {
		assert series.size() == datapoints;
		this.series = series;
	}

	public Series() {
		this.formatter = new SimpleDateFormat("yyyy-MM-dd");
	}

	public Indicator getIndicator() {
		return i;
	}

	public void setIndicator(Indicator i) {
		this.i = i;
	}

	public Date getFirstPeriod() {
		return firstPeriod;
	}
		
   /*
    * Expected format yyyy-mm-dd
    * */
	public void setBounds(String from, String to) throws BDCOMPException {
		try {
			this.firstPeriod = formatter.parse(from);
			this.lastPeriod = formatter.parse(to);
			
			Calendar startCalendar = new GregorianCalendar();
			startCalendar.setTime(firstPeriod);
			Calendar endCalendar = new GregorianCalendar();
			endCalendar.setTime(lastPeriod);

			int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
			int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
			this.datapoints = diffMonth + 1;			
		} catch (ParseException e) {
			BDCOMPException ex = new BDCOMPException(e);
			throw ex;
		}
		
	}

	public Date getLastPeriod() {
		return lastPeriod;
	}

}
