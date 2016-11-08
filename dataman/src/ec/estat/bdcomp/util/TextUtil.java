package ec.estat.bdcomp.util;

public class TextUtil {
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

}
