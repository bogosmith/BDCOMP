package ec.estat.bdcomp.util;

import ec.estat.bdcomp.BDCOMPException;
import ec.estat.bdcomp.data.Series;

public class TextUtils {
	public static String removeParentheses(String s) {
		StringBuffer res = new StringBuffer();
		int parenNesting = 0;
		for (int i = 0; i<s.length(); i ++) {
			char c = s.charAt(i);
			if (c == ('(') ) {					
				parenNesting += 1;
				continue;
			}
			if (c == (')') ) {					
				parenNesting -= 1;
				continue;
			}
			
			if (parenNesting < 1) {
				res.append(c);
			}			
		}
		return res.toString();		
	}
	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	public static Series.Country stringToCountry(String s) throws BDCOMPException {
		for (Series.Country c : Series.Country.values()) {
		    if (c.name().equals(s)) {
		         return c;
		    }
		    
	    }	
		if (s.equals("EU28")) {
	    	return Series.Country.EU;
	    }
	    if (s.equals("EA19")) {
	    	return Series.Country.EA;
	    }
	    throw new BDCOMPException("Country can't be matched.");
	}

}
