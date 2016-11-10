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
	private Calendar calendar = Calendar.getInstance();
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
		assert series.size() <= datapoints;		
		for (int i = datapoints - series.size(); i > 0; i --) {			
			series.add(Double.NaN);
		}
		assert series.size() == datapoints;	
		this.series = series;
	}

	public Series(Date firstPeriod, Date lastPeriod, Indicator i, Country c) {		
		this.firstPeriod = firstPeriod;
		this.lastPeriod = lastPeriod;
		this.i = i;
		this.c = c;
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
	
	public Double get(int i) {
		return series.get(i);		
	}

	public Date getFirstPeriod() {
		return firstPeriod;
	}

	public String toString(){
		StringBuffer res = new StringBuffer();
		if (i.getPeriodicity() == Indicator.Periodicity.MONTHLY) {
			calendar.setTime(firstPeriod);
			res.append(i.formatter.format(calendar.getTime()));
			res.append(" ");
			for (int k = 0; k < datapoints-1; k++) {
				calendar.add(Calendar.MONTH, 1);
				res.append(i.formatter.format(calendar.getTime()));
				res.append(" ");
			}			
		}	
		res.append("\n");
		res.append(c); res.append(" "); res.append(series.toString());
		return res.toString();
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
	private static void checkConsistency(Series s1, Series s2) throws BDCOMPException {
		if (! s1.getFirstPeriod().equals(s2.getFirstPeriod())) {throw new BDCOMPException ("Inconsistent first period of series.");};
		if (! s1.getLastPeriod().equals(s2.getLastPeriod())) {throw new BDCOMPException ("Inconsistent last period of series.");};
		if (! s1.getIndicator().equals(s2.getIndicator())) {throw new BDCOMPException ("Inconsistent indicators of series.");};		
		if (! s1.getCountry().equals(s2.getCountry())) {throw new BDCOMPException ("Different countries of series.");};
	}
	
	/*
	 * The merging compatible series is done according to the rule: If the new series revises an already existing value in the old series
	 * this new value is NOT taken into account. If the new series provides a value where there was none it IS taken into account. 
	 */
	public static Series mergeSeries(Series oldSeries, Series newSeries) throws BDCOMPException {
		checkConsistency(oldSeries, newSeries);
		
		Series res = new Series(oldSeries.getFirstPeriod(), oldSeries.getLastPeriod(), oldSeries.getIndicator(), oldSeries.getCountry());
		Vector<Double> data = new Vector<Double>();
		for (int i = 0; i < oldSeries.datapoints; i++) {			
			if (oldSeries.get(i).isNaN()) {
				data.add(newSeries.get(i));
			} else {
				data.add(oldSeries.get(i));
			}
		}
		res.setSeries(data);
		return res;
	}
	
	/*
	 * Compatible series are found and merged according to the rules.
	 */
	public static Vector<Series> mergeSetsOfSeries(Vector<Series> oldSet, Vector<Series> newSet) throws BDCOMPException {
		// !! This is masking problems with missing data
		if (newSet.size() == 0) { return oldSet;} 
		if (oldSet.size() != newSet.size()) {
			throw new BDCOMPException("Attempting to merge sets of series where the sets have different number of member series.");
		}
		Vector<Series> res = new Vector<Series>();
		for (Series s : oldSet) {
			for (Series t: newSet) {
				try {
					checkConsistency(s,t);
				} catch (BDCOMPException e){
					continue;
				}
				Series u = mergeSeries(s, t);
				res.add(u);
			}
		}
		if (res.size() != oldSet.size()) {
			throw new BDCOMPException("Couldn't match all series in the sets.");
		}
		return res;
	}

	public Date getLastPeriod() {
		return lastPeriod;
	}

}
