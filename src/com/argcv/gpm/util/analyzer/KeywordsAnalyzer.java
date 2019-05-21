package com.argcv.gpm.util.analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.util.Version;

public class KeywordsAnalyzer extends org.apache.lucene.analysis.Analyzer {

	/**
	 * a simple tokenizer , make all words in lower case and spilit them with space
	 */
	@Override
	protected TokenStreamComponents createComponents(final String str, final Reader reader) {
		Tokenizer source = new CommaTokenizer(Version.LUCENE_43, reader);
		TokenStream result= new KeywordsFilter(source);
        return new TokenStreamComponents(source,result);
	}
	
}
