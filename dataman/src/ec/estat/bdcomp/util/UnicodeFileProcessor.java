package ec.estat.bdcomp.util;

import ec.estat.bdcomp.data.Series;
import ec.estat.bdcomp.data.Indicator;
import ec.estat.bdcomp.BDCOMPException;

import java.io.File;
import java.util.Vector;
import java.util.StringTokenizer;

public class UnicodeFileProcessor extends FileProcessor {
	public UnicodeFileProcessor(Indicator i) {
		super(i);
	}

	
	public Vector<Series> processFile(File f) throws BDCOMPException {
		String[] contents = getContents(f);
		
		
		return null;
	}

}
