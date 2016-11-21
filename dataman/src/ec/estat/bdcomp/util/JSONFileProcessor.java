package ec.estat.bdcomp.util;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.json.JSONObject;

import ec.estat.bdcomp.BDCOMPException;
import ec.estat.bdcomp.data.Indicator;
import ec.estat.bdcomp.data.Series;


public class JSONFileProcessor extends FileProcessor {
	public JSONFileProcessor(Indicator i) {
		super(i);
	}
	public String getDirName() {
		return "sdmx";	
	}
	public Vector<Series> processFile(File f, Date firstPeriod, Date lastPeriod) throws BDCOMPException {
		Vector<Series> res = new Vector<Series>();
		Vector<String> countries = this.i.getCountryAbbreviations();
		
		String contents = getContents(f);
		JSONObject obj = new JSONObject(f);
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
			    	Double val = Double.parseDouble(values.getString(Integer.toString(i * periods + j)));
			    	//this.i.getFormatter().parse(invertedTimeIndex.get(Integer.toString(j)))
			    	seriesValues.put(Integer.toString(j),val);
			    }
			    res.add(s);
			}			
		}
		return res;
	}
	
	private HashMap<String, String> invert(JSONObject mapping) {
		HashMap<String, String> res = new HashMap<String, String>();
		for (String k : mapping.keySet()) {
			String val = (String) mapping.get(k);
			res.put(val,k);
		}
		return res;
	}
}
