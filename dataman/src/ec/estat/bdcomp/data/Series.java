package ec.estat.bdcomp.data;

import ec.estat.bdcomp.BDCOMPException;
import ec.estat.bdcomp.data.Indicator.Type;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;


public class Series {
	
	public enum Country {
		AT, BE, BG, HR, CY, CZ, DK, EE, ES, EL, FI, FR, DE, HU, IE, IT, LV, LT, LU, MT, NL, PL, PT, RO, SK, SI, SE, UK, EA, EU		
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
		if (series.size() > datapoints) {
			System.out.println(this.getClass().getName() + " Not enough datapoints. ");
			System.out.println(datapoints);
			System.out.println(series);
		}
		///assert series.size() <= datapoints;		
		for (int i = datapoints - series.size(); i > 0; i --) {			
			series.add(Double.NaN);
		}
		assert series.size() == datapoints;	
		this.series = series;
	}
	
	public void setSeries(HashMap<String,Double> series) {		
		//assert series.keySet().size() == datapoints;
		Vector<Double> s = new Vector<Double>();		
		if (i.getPeriodicity() == Indicator.Periodicity.MONTHLY) {
			calendar.setTime(firstPeriod);
			for (int k = 0; k < datapoints-1; k++) {
				calendar.add(Calendar.MONTH, k);
				String period = i.formatter.format(calendar.getTime());
				Double val = series.get(period);
				s.add((val == null? Double.NaN : val));	
				calendar.setTime(firstPeriod);
			}			
		}
		setSeries(s);		
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
	
	public static Vector<Series> choseFromDifferentProcessors(Vector<Series> v1, Vector<Series> v2) throws BDCOMPException {
		if (v1.size() == 0) {
			return v2;
		}
		if (v2.size() == 0) {
			return v1;
		}
		
		if ( compareVectors(v1, v2)) {
			if(v2.size() > v2.size()) {return v2;} else {return v1;}
		} else {
			throw new BDCOMPException("Different values of series.");
		}
		
		/*if(v1.size() != v2.size()) {
			System.out.println(v1.size() + " " + v2.size());
			throw new BDCOMPException("Different sizes of series.");
		} else {
			if ( compareVectors(v1, v2)) {
				return v1;
			} else {
				throw new BDCOMPException("Different values of series.");
			}
		}*/		
	}
	
	/*
	 *  This method is necessary since the natural implementation 
	 *  new HashSet<Series>(a).equals(new HashSet<Series>(b))
	 *  doesn't work for some reason.
	 * */
	
	private static <T> boolean compareVectors (Vector<T> a, Vector<T> b) {
		//System.out.println("AAA");
		outer:
		for (Enumeration<T> e = a.elements(); e.hasMoreElements();) {			
			T el = e.nextElement();
			for (Enumeration<T> f = b.elements(); f.hasMoreElements();) {
				T nextEl = f.nextElement();
				if (nextEl.equals(el)) {
					continue outer;
				} else if (el instanceof Series && (((Series)el).subseries((Series)nextEl) || ((Series) nextEl).subseries((Series)el) )) {
					continue outer;
				}
				
			}
			
			// Return true if one vector simply has a country missing.
			if (el instanceof Series) {
				for (Enumeration<T> f = b.elements(); f.hasMoreElements();) {
					Series nextEl = (Series) f.nextElement();
					if (((Series) el).c == nextEl.c) {
						System.out.println("AAA");
						System.out.println(el);
						System.out.println(nextEl);
						
						return false;
						}
				
				
				}
			}
			return true;
			//System.out.println("AAA");
			//System.out.println(el);
			//System.out.println("BBB");
			/*System.out.println(b.size());
			System.out.println(a.size());*/
			
			
			
		}
		outer:
		for (Enumeration<T> e = b.elements(); e.hasMoreElements();) {
			T el = e.nextElement();
			for (Enumeration<T> f = a.elements(); f.hasMoreElements();) {
				T nextEl = f.nextElement();
				if (nextEl.equals(el)) {
					continue outer;
				} else if (el instanceof Series && (((Series)el).subseries((Series)nextEl) || ((Series) nextEl).subseries((Series)el) )) {
					continue outer;
				}
			}

			// Return true if one vector simply has a country missing.
			if (el instanceof Series) {
				for (Enumeration<T> f = a.elements(); f.hasMoreElements();) {
					Series nextEl = (Series) f.nextElement();
					if (((Series) el).c == nextEl.c) {
						System.out.println("BBB");
						return false;
						
					}
				
				
				}
			}
			return true;
			//System.out.println("BBB");
			//throw new Error("BBBBB");
			//return false;
		}
		return true;
	}
	
	public static boolean compareSetsOfSeries(Vector<Series> set1, Vector<Series> set2) throws BDCOMPException {
		if(set1.size() != set2.size()) {throw new BDCOMPException("Trying to compare sets of series of different sizes.");}
		for (Series s: set1) {
			
			
		}
		
		throw new BDCOMPException("Not implemented.");
	}
	
	public boolean subseries (Series s1) {
		try {
			checkConsistency(this, s1);
		} catch (BDCOMPException ex) {
			//throw new RuntimeException(ex);
			return false;
		}
		
		Vector<Double> d1 = this.getSeries();
		Vector<Double> d2 = s1.getSeries();
		for (int i = 0; i < d1.size(); i ++) {
			Double i1 = d1.get(i);
			Double i2 = d2.get(i);
			// allow if one series is undefined to take the other series
			if (i1.isNaN() || i2.isNaN()) { continue;}
			
			if (Double.compare(i1, i2) != 0) {
				return false;
			}
		}
		return true;
		
	}
	
	public boolean equals(Object o) {
		
		if (! (o instanceof Series)) {return false;};
		Series s1 = (Series) o;
		try {
			checkConsistency(this, s1);
		} catch (BDCOMPException ex) {
			//throw new RuntimeException(ex);
			return false;
		}
		
		Vector<Double> d1 = this.getSeries();
		Vector<Double> d2 = s1.getSeries();
		for (int i = 0; i < d1.size(); i ++) {
			Double i1 = d1.get(i);
			Double i2 = d2.get(i);
			if (Double.compare(i1, i2) != 0) {
				/*System.out.println("Dump from class:" + this.getClass().getName());
				System.out.println("In first series: " + d1.get(i));
				System.out.println("In second series: " + d2.get(i));
				System.out.println(o);
				System.out.println(this);*/
				return false;
			}
		}
		return true;
	}

	public String toString() {
		//if ( 0==0) throw new RuntimeException("AAAA");
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
		if (i.getType() == Type.INDEX) {
			//if ( 0==0) throw new RuntimeException("BBBB");
			res.append(c); res.append(" "); res.append(series.toString());
		} else if(i.getType() == Type.COUNT) {
			//if ( 0==0) throw new RuntimeException("CCCC");
			Vector<Integer> intseries = new Vector<Integer>();
			for (int i = 0; i < series.size(); i ++) {
				intseries.add((int)(series.get(i).doubleValue()));
			}
			res.append(c); res.append(" "); res.append(intseries.toString());
			
		}
		return res.toString();
	}
	
	public static String printVectorOfSeries(Vector<Series> vec) {
		StringBuffer res = new StringBuffer();
		Indicator i = vec.get(0).i;
		Calendar calendar = vec.get(0).calendar;
		Date firstPeriod = vec.get(0).firstPeriod;
		int datapoints = vec.get(0).datapoints;
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
		for (Series s: vec) {
			res.append(s.c); res.append(" ");
			for (double d : s.series) {
				//if (Double.isNaN(d)) {res.append("-");} else { 
				if (i.getType() == Type.INDEX || ((Double) d).isNaN()) {
					res.append(d); res.append(" ");
				} else if(i.getType() == Type.COUNT) {
					res.append((int)d); res.append(" ");
				}
				//}
			}	
			res.append("\n");
		}
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
		//System.out.println(s1.getCountry() + "##" + s2.getCountry());
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
				// Here one can get notified where revisions take place.
				if ( Double.compare(newSeries.get(i),oldSeries.get(i)) != 0 ) {
					//System.out.println(Series.class.getName() + " Revision identified:" + newSeries.getCountry() + " " + i + " " + oldSeries.get(i) + " " + newSeries.get(i));
					//throw new BDCOMPException("test");
					//System.out.println(newSeries.getCountry() + " " + i + " " + oldSeries.get(i) + " " + newSeries.get(i));
				}
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
		if (newSet.size() == 0) {
			System.out.println(Series.class.getName() + "AAA");
			return oldSet;
			} 
		
		/*if (oldSet.size() != newSet.size()) {
			System.out.println(oldSet.size() + " " + newSet.size());
			throw new BDCOMPException("Attempting to merge sets of series where the sets have different number of member series.");
		}*/
		
		//System.out.println(oldSet.size() + " " + newSet.size());
		Vector<Series> res = new Vector<Series>();
		outer:
		for (Series s : oldSet) {
			inner:
			for (Series t: newSet) {
				try {
					checkConsistency(s,t);
				} catch (BDCOMPException e){					
					continue inner;
				}
				Series u = mergeSeries(s, t);
				res.add(u);
				continue outer;
			}
		
			//if the series couldn't be matched
			res.add(s);
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
