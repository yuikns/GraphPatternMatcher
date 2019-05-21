package com.argcv.gpm.util.analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.util.CharTokenizer;
import org.apache.lucene.util.Version;

public class CommaTokenizer extends CharTokenizer {
	public CommaTokenizer(Version matchVersion, Reader in) {
		super(matchVersion, in);
	}
	
	@Override
	protected boolean isTokenChar(int c) {
		//return !(c == 44);
		return !(c == ' ' || c == ' ');
	}

}