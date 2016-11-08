package ec.estat.bdcomp.util;

import ec.estat.bdcomp.data.Series;
import ec.estat.bdcomp.data.Indicator;
import ec.estat.bdcomp.BDCOMPException;

import java.io.File;
import java.util.Vector;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.LinkedList;


public class UnicodeFileProcessor extends FileProcessor {
	public UnicodeFileProcessor(Indicator i) {
		super(i);
	}

/*
 * For HICP we use EU and EA respectively rather than EU28 and EA19.
 * */	

	
	public Vector<Series> processFile(File f, Date firstPeriod, Date lastPeriod) throws BDCOMPException {
		String[] contents = getContents(f);
		Vector<String> countries = this.i.getCountryAbbreviations();
		Vector<Series> res = new Vector<Series>();
		for (String l: contents) {
			String noParen = TextUtil.removeParentheses(l);
			StringTokenizer tknz = new StringTokenizer(noParen);
			Vector<String> toks = new Vector<String>();
			while(tknz.hasMoreElements()) {
				toks.add(tknz.nextToken());
			}			
			if (countries.contains(toks.get(0))) {
				LinkedList<Double> values = new LinkedList<Double>();
				Series s = new Series(firstPeriod, lastPeriod, i);
				for (int i = toks.size() - 1; i > 0; i --) {					
					if(TextUtil.isNumeric(toks.get(i))) {
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
