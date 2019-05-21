package com.argcv.gpm.util.analyzer;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class KeywordsAnalyzerTest {
	public void getAnalyzersResult(String str, Analyzer a){
		try{
			TokenStream ts = a.tokenStream("cotents", new StringReader(str));
			ts.reset();
			while(ts.incrementToken()){
				CharTermAttribute cta = ts.addAttribute(CharTermAttribute.class);
				System.out.println(">$"+cta.toString()+"[CR]");
			}
			ts.end();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static void main(String []args) throws IOException{
		Analyzer ka = new KeywordsAnalyzer();
		//String testString = "关键词1,关键词2,关键词3,关键词4,,全角逗号左边，全角逗号右边  ,全角顿号右边、全角顿号右边,其他";
		String testString = "模糊关系;;模糊算子;;失真度";
		KeywordsAnalyzerTest kat = new KeywordsAnalyzerTest();
		kat.getAnalyzersResult(testString, ka);
		//kat.getAnalyzersResult(testString, new IKAnalyzer(true));
		//kat.getAnalyzersResult(testString, new SimpleAnalyzer(Version.LUCENE_43));
	}
}
