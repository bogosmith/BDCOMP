package ec.estat.bdcomp.util;

import java.io.File;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Set;
import java.util.HashSet;

import ec.estat.bdcomp.BDCOMPException;
import ec.estat.bdcomp.data.Series;

public class DirectoryProcessor {
	
	private static String fileNamePattern = "^\\d\\d\\d\\d-\\d\\d-\\d\\d$";
	private static Pattern r;
	static {
		r = Pattern.compile(fileNamePattern);
	}
	
	public static Vector<Series> processDirectory(File dir, FileProcessor processor) throws BDCOMPException {
		Vector<Series> res = new Vector<Series>();
		File[] filesList = filter(dir.listFiles());
		Arrays.sort(filesList);
		for (File subdir : filesList) {
			System.out.println(subdir.getName());
			File properDir = chooseFormat(subdir, processor);
			File dataFile = null;
			try {
				dataFile = chooseFileForIndicator(properDir, processor);
				Vector<Series> s = processor.processFile(dataFile);
				if (res.size() == 0) {
					res = s;
				} else {				
					res = Series.mergeSetsOfSeries(res, s);
				}
			} catch (BDCOMPException ex) {
				//!! This is masking problems with missing data
				continue;
			}			
		}
		return res;
	}
	
	// process using all the processors in order. Signals any discrepancies.
	public static Vector<Series> processDirectory(File dir, Vector<FileProcessor> processors) throws BDCOMPException {
		Vector<Series> res = new Vector<Series>();
		File[] filesList = filter(dir.listFiles());
		Arrays.sort(filesList);
		for (File subdir : filesList) {
			System.out.println(subdir.getName());
			Vector<Series> s = null;
			for (FileProcessor proc : processors) {
				//System.out.println(proc.getClass().getName());
				File properDir = chooseFormat(subdir, proc);
				File dataFile = null;
				try {
					dataFile = chooseFileForIndicator(properDir, proc);
				} catch (BDCOMPException ex) {
					//!! This is masking problems with missing data - namely missing files
					continue;
					//throw ex;
					//s = new Vector<Series>();
				}			
				Vector<Series> s1 = proc.processFile(dataFile);		
				//System.out.println(s1);
				if (s == null) {s = s1;} 
				else {
					s = Series.choseFromDifferentProcessors(s, s1);
					/*if (!compareVectors(s, s2)){throw new BDCOMPException("Different series.");}
					if (!compareVectors(s1, s2)){throw new BDCOMPException("Different series.");}*/
				
				}
				
				if (res.size() == 0) {
					res = s;
				} else {					
					res = Series.mergeSetsOfSeries(res, s);
				}
			}
			
		}
		return res;
	}
	
	

	
	private static File[] filter(File[] filesList) {		
		Vector<File> res = new Vector<File>();
		for (int i = 0; i < filesList.length; i++) {
			Matcher m = r.matcher(filesList[i].getName());
			if (m.find()) {res.add(filesList[i]);}
		}
		File[] r = new File[res.size()];
        return res.toArray(r);	
	}
	
	/*
	 * The crawler stores for each day data in (three) different directories depending on the web service used and the format of the file.
	 * Currently (10.11.2016) options are unicode, sdmx and json. Each file processor is interested in the corresponding directory. A JsonFileProcessor
	 * in the json directory, a UnicodeProcessor in the unicode directory and the SDMXProcessor sdmx. 
	 */
	private static File chooseFormat (File dir, FileProcessor processor) throws BDCOMPException {
		File[] filesList = dir.listFiles();		
		for (File f : filesList) {		
			if (f.getName().equals(processor.getDirName())) {
				return f;
			}
		}
		throw new BDCOMPException("Can't find appropriate directory for processor " + processor.getDirName() + " in direcotry " + dir.getAbsolutePath() + " .");
	}
	
	private static File chooseFileForIndicator(File dir, FileProcessor processor) throws BDCOMPException {
		File[] filesList = dir.listFiles();
		for (File f : filesList) {
			if (f.getName().equals(processor.i.getFilename())) {
				return f;
			}
		}
		throw new BDCOMPException("Can't find appropriate file for processor " + processor.i.getFilename() + " in " + dir.getAbsolutePath() + " .");
		
	}

}
