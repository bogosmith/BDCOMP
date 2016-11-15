package ec.estat.bdcomp;

import ec.estat.bdcomp.data.*;
import ec.estat.bdcomp.util.*;

import org.json.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Vector;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
		//UnicodeFileProcessor ufp = new UnicodeFileProcessor(h);
		SDMXFileProcessor ufp = new SDMXFileProcessor(h);
		//File f = new File("Z:\\bdcomp_data\\2016-11-08\\unicode\\hicp");
		File f = new File("Z:\\bdcomp_data\\2016-11-08\\sdmx\\hicp");
		
		Vector<Series> v = ufp.processFile(f);		
		Series s1 = v.get(0);
		Series s2 = v.get(1);
		System.out.println(Series.mergeSeries(s1, s2));
		*/
		
		/*
		SDMXFileProcessor ufp = new SDMXFileProcessor(new HICP(false));
		File f = new File("Z:\\bdcomp_data\\2016-11-08\\sdmx\\hicp");
		DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();		
		DocumentBuilder db = df.newDocumentBuilder();
		
		Document doc = db.parse(f);
		Node dataSetNode = doc.getChildNodes().item(0).getChildNodes().item(3);	
		for (int i = 0; i < dataSetNode.getChildNodes().getLength(); i++ ) {
			Node next = dataSetNode.getChildNodes().item(i);
			if (next.getNodeName().equals("generic:Series")) {
				Node seriesKey = next.getFirstChild().getNextSibling();
				NodeList children = seriesKey.getChildNodes();
				for (int j = 0 ; j< children.getLength(); j ++) {
					Node n2 = children.item(j);					
					NamedNodeMap attr = n2.getAttributes();
					if (attr == null) {
						continue;
					}
					Node id = attr.getNamedItem("id");
					System.out.println(id.getFirstChild().getTextContent());
					
				}
				
				
			}
		}
		*/
		
		
		/*
		HashMap<String, Integer> h = new HashMap<String,Integer>();
		fillMap(h);
		System.out.println(h.get(new SimpleDateFormat("yyyy-MM").parse("2000-01-01").toString()));
		*/
		
		
		/*Process p = new Process();
		p.dummymethod();*/
		
		
		//System.out.println(s2);
		
		
		//UnicodeFileProcessor f = new UnicodeFileProcessor(new HICP(false));
		SDMXFileProcessor f = new SDMXFileProcessor(new HICP(false));
		//UnicodeFileProcessor f = new UnicodeFileProcessor(new Retail(false));
		File dir = new File("C:\\Users\\kovacbo\\bdcomp\\bdcomp_data\\");
		Vector<Series> seriesHicp = DirectoryProcessor.processDirectory(dir, f);
		for (int i = 0; i < seriesHicp.size(); i ++ ) {
			System.out.println(seriesHicp.get(i));
		}
		
		
		/*LinkedList<Double> values = new LinkedList<Double>();
		values.add(1.0);
		values.add(2.0);
		values.add(3.0);
		values.poll();
		values.poll();
		System.out.println(values);
		*/
		
		//System.out.println(f.getIndicator());
		
		
		
        /*String line = "2010-09-27";
	    String pattern = "^\\d\\d\\d\\d-\\d\\d-\\d\\d$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        System.out.println(m.find());*/
		
		
	}
	private static void fillMap(HashMap<String,Integer> hm) throws Throwable {
		hm.put(new SimpleDateFormat("yyyy-MM").parse("2000-01-01").toString(), 134);
	}
	
	public void dummymethod() {
		Process p = new Process();
		if (this.getClass().isInstance(p)) {System.out.println("yes");};
		
	}

}
