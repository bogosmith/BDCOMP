package ec.estat.bdcomp;

import ec.estat.bdcomp.data.*;
import ec.estat.bdcomp.util.*;

import org.json.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Set;
import java.util.Vector;
import java.util.HashMap;
import java.util.HashSet;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Enumeration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;

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
	
	private static String readFile(String path) throws Throwable {
		BufferedReader br = null;
		StringBuffer res = new StringBuffer();		
		br = new BufferedReader(new FileReader(new File(path)));		    
		String line = br.readLine();		    
        while (line != null) {
	     	res.append(line.trim());	        
		    line = br.readLine();
		}
		br.close();
		return (res.toString());	
		
	}

	
	public static void main (String[] args) throws Throwable {
		/*String indir = args[0];
		String outdir = args[1];
		System.out.println(indir);
		System.out.println(outdir);
		*/
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
		
		
		//System.out.println(new SimpleDateFormat("yyyy-MM").parse("2015M11").toString());
		//HICP ix = new HICP(false);
		//HICPExclEnergy ix = new HICPExclEnergy(false);
		//Retail ix = new Retail(false);
		//Retail ix = new Retail(true);
		//RetailExclFuel ix = new RetailExclFuel(false);
		RetailExclFuel ix = new RetailExclFuel(true);
		//Unemployment ix = new Unemployment(true);
		//Unemployment ix = new Unemployment(false);
		//TourismAllAccomodation ix = new TourismAllAccomodation(false);
		//TourismHotels ix = new TourismHotels(false);
		
		UnicodeFileProcessor f1 = new UnicodeFileProcessor(ix);
		SDMXFileProcessor f2 = new SDMXFileProcessor(ix);
		JSONFileProcessor f3 = new JSONFileProcessor(ix);
		ix.getFormatter().parse("2015-11");
		//Unicode	FileProcessor f = new UnicodeFileProcessor(new Retail(false));
		File dir = new File("C:\\Users\\Stanislava\\git\\BDCOMP\\data\\bdcomp_data\\");		
		//File dir = new File("H:\\bdcomp\\bdcomp_test_data7\\");
		Vector<FileProcessor> processors = new Vector<FileProcessor>();
		processors.add(f1);
		//processors.add(f2);
		//processors.add(f3);
		Vector<Series> seriesHicp = DirectoryProcessor.processDirectory(dir, processors);
		
		/*for (Series ser : seriesHicp) {
			System.out.println(ser);
		}*/
		
		System.out.println(Series.printVectorOfSeries(seriesHicp));
		
		//Vector<Series> seriesHicpUni = DirectoryProcessor.processDirectory(dir,f1 );
		//Vector<Series> seriesHicpSDMX = DirectoryProcessor.processDirectory(dir,f2 );

		
		//System.out.println(new HashSet<Series>(seriesHicpUni).equals(new HashSet<Series>(seriesHicpSDMX)));
		
		//System.out.println(compareVectors(seriesHicpUni, seriesHicpSDMX));
		
		/*String s1 = "a";
		String t1 = "a";
		String s2 = "b";
		String t2 = "b";
		Vector<String> vec1 = new Vector<String>();
		Vector<String> vec2 = new Vector<String>();
		vec1.add(s1);
		vec1.add(s2);
		vec2.add(t2);
		vec2.add(t1);
		System.out.println(new HashSet<String>(vec1).equals(new HashSet<String>(vec2)));
		*/
		
		
		
		/*for (int i = 0; i < seriesHicpUni.size(); i ++ ) {
			System.out.println(seriesHicpSDMX.get(i));
		}*/
		
		
		/*
		JSONObject obj = new JSONObject(" { \"firstName\": \"John\",  \"lastName\": \"Smith\",  \"age\": 25,   \"address\": { \"streetAddress\": \"21 2nd Street\",\"city\": \"New York\",  \"state\": \"NY\",  \"postalCode\": 10021  }, \"phoneNumbers\": [   {   \"type\": \"home\",  \"number\": \"212 555-1234\"   },  {  \"type\": \"fax\",  \"number\": \"646 555-4567\"  }]}");
		String pageName = obj.getJSONObject("address").getString("streetAddress");
		System.out.println(pageName);
		*/
		
		/*
		String jsonString = readFile("C:\\Users\\kovacbo\\bdcomp\\bdcomp_data\\2016-01-20\\json\\hicp");
		//System.out.println(jsonString);
		JSONObject obj = new JSONObject(jsonString);
		
		JSONObject index = obj.getJSONObject("dimension").getJSONObject("geo").getJSONObject("category").getJSONObject("index");
		System.out.println(index.keySet());
		System.out.println(index.get("UK"));
		JSONObject values = obj.getJSONObject("value");
		System.out.println(values.get("1"));
		*/
		
		
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
	
	public static <T> boolean compareVectors (Vector<T> a, Vector<T> b) {
		outer:
		for (Enumeration<T> e = a.elements(); e.hasMoreElements();) {
			T el = e.nextElement();
			for (Enumeration<T> f = b.elements(); f.hasMoreElements();) {
				if (f.nextElement().equals(el)) {
					continue outer;
				}
			}
			return false;
		}
		outer:
		for (Enumeration<T> e = b.elements(); e.hasMoreElements();) {
			T el = e.nextElement();
			for (Enumeration<T> f = a.elements(); f.hasMoreElements();) {
				if (f.nextElement().equals(el)) {
					continue outer;
				}
			}
			return false;
		}
		return true;
	}
	
	private static void fillMap(HashMap<String,Integer> hm) throws Throwable {
		hm.put(new SimpleDateFormat("yyyy-MM").parse("2000-01-01").toString(), 134);
	}
	
	public void dummymethod() {
		Process p = new Process();
		if (this.getClass().isInstance(p)) {System.out.println("yes");};
		
	}

}
