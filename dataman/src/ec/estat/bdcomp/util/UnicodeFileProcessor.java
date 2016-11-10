package ec.estat.bdcomp.util;

import ec.estat.bdcomp.data.Series;
import ec.estat.bdcomp.data.Indicator;
import ec.estat.bdcomp.BDCOMPException;
import ec.estat.bdcomp.util.TextUtils;

import java.io.File;
import java.util.Vector;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.LinkedList;


public class UnicodeFileProcessor extends FileProcessor {
	public UnicodeFileProcessor(Indicator i) {
		super(i);
	}
	public String getDirName() {
		return "unicode";	
	}
/*
 * For HICP we use EU and EA respectively rather than EU28 and EA19.
 * */	

	
	public Vector<Series> processFile(File f, Date firstPeriod, Date lastPeriod) throws BDCOMPException {
		String[] contents = getContents(f);
		Vector<String> countries = this.i.getCountryAbbreviations();
		Vector<Series> res = new Vector<Series>();
		for (String l: contents) {
			String noParen = TextUtils.removeParentheses(l);
			StringTokenizer tknz = new StringTokenizer(noParen);
			Vector<String> toks = new Vector<String>();
			while(tknz.hasMoreElements()) {
				toks.add(tknz.nextToken());
			}
			// this ignores lines that have nothing after the removal of parentheses
			if (toks.size() == 0 ) { continue; }
			
			String country = toks.get(0);
			if (countries.contains(country)) {
				LinkedList<Double> values = new LinkedList<Double>();
				Series s = new Series(firstPeriod, lastPeriod, i, TextUtils.stringToCountry(country));
				for (int i = toks.size() - 1; i > 0; i --) {					
					if(TextUtils.isNumeric(toks.get(i))) {
						values.add(0, Double.parseDouble(toks.get(i)));
					}					
				}
				s.setSeries(new Vector<Double>(values));
				res.add(s);				
			}			
		}		
		return res;
	}
}
