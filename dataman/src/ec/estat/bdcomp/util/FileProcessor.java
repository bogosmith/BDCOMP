package ec.estat.bdcomp.util;

import ec.estat.bdcomp.data.Series;
import ec.estat.bdcomp.data.Indicator;
import ec.estat.bdcomp.BDCOMPException;

import java.util.Date;
import java.util.Vector;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;;


public abstract class FileProcessor {
	protected Indicator i;
	
	public FileProcessor(Indicator i){
		this.i = i;
	}		
	
	public abstract String getDirName();
	public abstract Vector<Series> processFile (File f, Date firstPeriod, Date lastPeriod) throws BDCOMPException;
	
	public Vector<Series> processFile(File f) throws BDCOMPException {
		try {
			return processFile(f,  new SimpleDateFormat("yyyy-MM-dd").parse("2016-01-01"),  new SimpleDateFormat("yyyy-MM-dd").parse("2016-12-01"));
		} catch (ParseException p) {
			throw new BDCOMPException(p);			
		}
	}

	
	public Indicator getIndicator() {
		return i;
	}
	/*
	 * Returns the contents of the file as an array of Strings.
	 * */
	public static String[] getContents(File f) throws BDCOMPException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));		
		    Vector<String> lines = new Vector<String>();
		    String line = br.readLine();

		    while (line != null) {
		        lines.add(line);		        
		        line = br.readLine();
		    }
		    br.close();
		    return (lines.toArray(new String[lines.size()]));		    
		}
		catch (FileNotFoundException e) {
			BDCOMPException ex = new BDCOMPException(e);
			throw ex;
		}
		catch (IOException e) {
			try {
				br.close();
			} catch (IOException inner_e) {
				BDCOMPException ex = new BDCOMPException(inner_e);
				throw ex;
			}
			BDCOMPException ex = new BDCOMPException(e);			
			throw ex;
		}	
	}
	
	
	
}
