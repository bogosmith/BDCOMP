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
	
	private final Indicator i;
	private final Date firstPeriod;
	private final Date lastPeriod;
	private final int datapoints;
	private SimpleDateFormat formatter;
	private Vector<Double> series;
	private Country c;
		
	public Country getCountry() {
		return c;
	}

	public void setCountry(Country c) {
		this.c = c;
	}

	public Vector<Double> getSeries() {		
		return series;
	}

	public void setSeries(Vector<Double> series) {
		assert series.size() == datapoints;
		this.series = series;
	}

	public Series(Date firstPeriod, Date lastPeriod, Indicator i) {		
		this.firstPeriod = firstPeriod;
		this.lastPeriod = lastPeriod;
		this.i = i;
		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(firstPeriod);
		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(lastPeriod);

		int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
		this.datapoints = diffMonth + 1;
		this.formatter = new SimpleDateFormat("yyyy-MM-dd");
	}

	public Indicator getIndicator() {
		return i;
	}

	public Date getFirstPeriod() {
		return firstPeriod;
	}
		
   /*
    * Expected format yyyy-mm-dd
    * */
	/*public void setBounds(String from, String to) throws BDCOMPException {
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
		
	}*/

	public Date getLastPeriod() {
		return lastPeriod;
	}

}
