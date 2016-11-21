package ec.estat.bdcomp.util;

import java.text.ParseException;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.json.JSONObject;
import org.json.JSONException;

import ec.estat.bdcomp.BDCOMPException;
import ec.estat.bdcomp.data.Indicator;
import ec.estat.bdcomp.data.Series;


public class JSONFileProcessor extends FileProcessor {
	public JSONFileProcessor(Indicator i) {
		super(i);
	}
	public String getDirName() {
		return "json";	
	}
	public Vector<Series> processFile(File f, Date firstPeriod, Date lastPeriod) throws BDCOMPException {
		Vector<Series> res = new Vector<Series>();
		Vector<String> countries = this.i.getCountryAbbreviations();
		
		String contents = getContents(f);
		//JSONObject obj = new JSONObject(f);
		//System.out.println(contents);
		try {
			JSONObject obj = new JSONObject(contents);			
			JSONObject geoIndex = obj.getJSONObject("dimension").getJSONObject("geo").getJSONObject("category").getJSONObject("index");
			//int countryCount = geoIndex.keySet().size();
			JSONObject timeIndex = obj.getJSONObject("dimension").getJSONObject("time").getJSONObject("category").getJSONObject("index");
			HashMap<String, String> invertedTimeIndex = invert(timeIndex);
			
			int periods = timeIndex.keySet().size();
			JSONObject values = obj.getJSONObject("value");
			
			for(String geo : geoIndex.keySet()) {
				if (countries.contains(geo)) {
				    Series s = new Series(firstPeriod, lastPeriod, this.i, TextUtils.stringToCountry(geo));
				    HashMap<String, Double> seriesValues = new HashMap<String, Double>();
				    int i = (int)geoIndex.get(geo);
				    for (int j = 0; j < periods; j ++) {
				    	/*System.out.println("AAAA" + i * periods + j + "AAAA");
				    	String _s = Integer.toString(i * periods + j);
				    	System.out.println("BBBB" + _s + "BBBB");
				    	System.out.println("CCCC" + values.get(_s) + "CCCC");
				    			//getString(_s) + "CCCC");
				    	String __s = values.getString(_s);
				    	Double val = Double.parseDouble(__s);*/
				    	String key = Integer.toString(i * periods + j);
				    	Double val = Double.NaN;
				    	if ( values.has(key)) {				    		
				    		val = (Double) values.get(key);
				    	}
				    	//this.i.getFormatter().parse(invertedTimeIndex.get(Integer.toString(j)))
				    	String periodString = invertedTimeIndex.get(Integer.toString(j));
				    	//System.out.println(periodString);
				    	//System.out.println(this.i.getFormatter().parse(periodString) + " " + val);
				    	try {
				    		seriesValues.put(this.i.getFormatter().format(this.i.getFormatter().parse(periodString)),val);
				    	} catch (ParseException e) {
				    		throw new BDCOMPException(e);
				    	}
				    }
				    s.setSeries(seriesValues);
				    res.add(s);
				}			
			}
			return res;
		} catch (JSONException e) {
			throw new BDCOMPException(e);
		}
	}
	
	private HashMap<String, String> invert(JSONObject mapping) {
		HashMap<String, String> res = new HashMap<String, String>();
		for (String k : mapping.keySet()) {
			String val = mapping.get(k).toString();
			k = k.replace("M", "-");
			//k = k + "-01";
			res.put(val,k);
		}
		return res;
	}
}
