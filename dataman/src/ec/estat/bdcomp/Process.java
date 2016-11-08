package ec.estat.bdcomp;

import org.json.*;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

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
		listFilesForFolder(new File(indir));

		
		
		
		//File f = new File("");
		
		/*String str = "{ \"name\": \"Alice\", \"age\": 20 }";
		JSONObject obj = new JSONObject(str);
		String n = obj.getString("name");
		int a = obj.getInt("age");
		System.out.println(n + " " + a);  // prints "Alice 20"*/
		
	}

}
