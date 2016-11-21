package ec.estat.bdcomp.util;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.Vector;
import java.util.HashMap;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

import ec.estat.bdcomp.BDCOMPException;
import ec.estat.bdcomp.data.Indicator;
import ec.estat.bdcomp.data.Series;

public class SDMXFileProcessor extends FileProcessor {
	public SDMXFileProcessor(Indicator i) {
		super(i);
	}
	public String getDirName() {
		return "sdmx";	
	}
	
	private String getCountry(Node seriesKey) throws BDCOMPException {		
		NodeList children = seriesKey.getChildNodes();
		for(int i =0; i < children.getLength(); i ++) {
			Node next = children.item(i);
			NamedNodeMap attr = next.getAttributes();
			if (attr == null) {
				continue;
			}
			Node id = attr.getNamedItem("id");
			if (id.getFirstChild().getTextContent().equals("GEO")) {
				return attr.getNamedItem("value").getFirstChild().getTextContent();				
			}			
		}
		throw new BDCOMPException("Country couldn't be found from series key.");
	} 
	
	private Node getSeriesKey(Node series) throws BDCOMPException {		
		NodeList children = series.getChildNodes();
		for(int i =0; i < children.getLength(); i ++) {
			Node next = children.item(i);
			if (next.getNodeName().equals("generic:SeriesKey")) {
				return next;
			}			
		}
		throw new BDCOMPException("Series key couldn't be found from series.");
	}	
	
	private void processObservation(Node obs, HashMap<String,Double> d) throws BDCOMPException {
		Date period = null;
		Double value = null;
		NodeList children = obs.getChildNodes();
		for(int i =0; i < children.getLength(); i ++) {
			Node next = children.item(i);
			if (next.getNodeName().equals("generic:ObsDimension")) {
				NamedNodeMap attr = next.getAttributes();
				Node per = attr.getNamedItem("value");
				try {
					period = this.i.getFormatter().parse((per.getFirstChild().getTextContent()));
				}  catch (ParseException e) {					
					throw new BDCOMPException(e);
				}			
			} 
			if (next.getNodeName().equals("generic:ObsValue")) {
				NamedNodeMap attr = next.getAttributes();
				Node per = attr.getNamedItem("value");
				value = Double.parseDouble(((per.getFirstChild().getTextContent())));				
			}			
		}
		if (period == null) {
			throw new BDCOMPException("Couldn't get period from observation");
		}
		if (value == null) {
			throw new BDCOMPException("Couldn't get value from observation");			
		}
		d.put(this.i.getFormatter().format(period), value);
		
	}
	
	private Series processSeries(Node series, String country, Date firstPeriod, Date lastPeriod) throws BDCOMPException {	
				
		Series s = new Series(firstPeriod, lastPeriod, i, TextUtils.stringToCountry(country));
		
		HashMap<String, Double> values = new HashMap<String, Double>();
		NodeList children = series.getChildNodes();
		for(int i =0; i < children.getLength(); i ++) {
			Node next = children.item(i);
			if (next.getNodeName().equals("generic:Obs")) {				
				processObservation(next, values);
			}			
		}
		
		s.setSeries(values);
		return s;		
	} 
	
	public Vector<Series> processFile(File f, Date firstPeriod, Date lastPeriod) throws BDCOMPException {
		try {			
			Vector<Series> res = new Vector<Series>();
			Vector<String> countries = this.i.getCountryAbbreviations();
			
			DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();		
			DocumentBuilder db = df.newDocumentBuilder();
			
			Document doc = db.parse(f);
			Node root = doc.getChildNodes().item(0);			
			Node dataSetNode =null;
			for (int i = 0; i < root.getChildNodes().getLength(); i ++) {
				if (root.getChildNodes().item(i).getNodeName().equals("message:DataSet")) {
					dataSetNode = root.getChildNodes().item(i);
				}
			}
			if (dataSetNode == null) {
				return res;
			}
			
			for (int i = 0; i < dataSetNode.getChildNodes().getLength(); i++ ) {
				Node next = dataSetNode.getChildNodes().item(i);
				if (next.getNodeName().equals("generic:Series")) {					
					Node seriesKey = getSeriesKey(next);
					String country = getCountry(seriesKey);
					if (countries.contains(country)) { 
						Series s = processSeries(next, country, firstPeriod, lastPeriod);
						res.add(s);
					}
				}
			}
			
			//System.out.println(series1Node.getNodeName());			
			//System.out.println(doc.getChildNodes().item(0).getChildNodes().item(3));
			
			return res;
		} catch (ParserConfigurationException e) {
			throw new BDCOMPException(e);
		} catch (IOException e) {
			throw new BDCOMPException(e);
		} catch (SAXException e) {
			throw new BDCOMPException(e);
		}
	
	}
}
