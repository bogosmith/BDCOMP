package ec.estat.bdcomp;

import ec.estat.bdcomp.data.*;
import ec.estat.bdcomp.util.*;

import org.json.*;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Process {
	
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
	public static void listFilesForFolder(final File folder) {
	    for (final File fileEntry : folder.listFiles()) {
	        /*if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {*/
	            System.out.println(fileEntry.getName());
	        /*}*/
	    }
	}
	
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
	public static void testCalendar(String from, String to) throws Throwable {
		Date firstPeriod = formatter.parse(from);
		Date lastPeriod = formatter.parse(to);
		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(firstPeriod);
		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(lastPeriod);

		int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
		System.out.println(diffMonth);	
		
	}

	
	public static void main (String[] args) throws Throwable {
		String indir = args[0];
		String outdir = args[1];
		System.out.println(indir);
		System.out.println(outdir);
		/*Date d = new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01");
		System.out.println(d);*/
		//testCalendar("2011-01-12", "2012-02-01");
		
		//listFilesForFolder(new File(indir));
		
		/*String s = "	EA	Euro area (EA11-2000, EA12-2006, EA13-2007, EA15-2008, EA16-2010, EA17-2013, EA18-2014, EA19)	117.98	117.96	116.25	116.44	117.88	117.93	118.36	118.56	117.9	118	118.46	(:)";
		String t = removeParentheses(s);
		System.out.println(t);
		StringTokenizer tknz = new StringTokenizer(t);
		while(tknz.hasMoreElements()) {
			System.out.println(tknz.nextElement());
		}*/
		
		//File f = new File("");
		
		/*String str = "{ \"name\": \"Alice\", \"age\": 20 }";
		JSONObject obj = new JSONObject(str);
		String n = obj.getString("name");
		int a = obj.getInt("age");
		System.out.println(n + " " + a);  // prints "Alice 20"*/
		
		/*Indicator i = new HICP();
		System.out.println(HICP.Countries.values());*/
		
		/*Vector<String> v = new Vector<String>();
		v.add("a");
		System.out.println(v.contains("a"));*/
		
		/*HICP h = new HICP(false);
		UnicodeFileProcessor ufp = new UnicodeFileProcessor(h);
		File f = new File("Z:\\bdcomp_data\\2016-11-08\\unicode\\hicp");
		Vector<Series> v = ufp.processFile(f);		
		Series s1 = v.get(0);
		Series s2 = v.get(1);
		System.out.println(Series.mergeSeries(s1, s2));*/
		
		
		/*Process p = new Process();
		p.dummymethod();*/
		
		
		//System.out.println(s2);
		
		UnicodeFileProcessor f = new UnicodeFileProcessor(new HICP(false));
		File dir = new File("C:\\Users\\kovacbo\\bdcomp\\bdcomp_data\\");
		Vector<Series> seriesHicp = DirectoryProcessor.processDirectory(dir, f);
		System.out.println(seriesHicp.get(2));

        /*String line = "2010-09-27";
	    String pattern = "^\\d\\d\\d\\d-\\d\\d-\\d\\d$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        System.out.println(m.find());*/
		
		
	}
	public void dummymethod() {
		Process p = new Process();
		if (this.getClass().isInstance(p)) {System.out.println("yes");};
		
	}

}
