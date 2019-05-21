package com.argcv.gpm.util;

import java.util.HashSet;
import java.util.Set;

public class ReservedWordEscape {
	final static String[] reservedWordList = { "abstract", "boolean", "break",
			"byte", "case", "catch", "char", "class", "continue", "default",
			"do", "double", "else", "extends", "false", "final", "finally",
			"float", "for", "if", "implements", "import", "instanceof", "int",
			"interface", "long", "native", "new", "null", "package", "private",
			"protected", "public", "return", "short", "static", "super",
			"switch", "synchronized", "this", "throw", "throws", "transient",
			"true", "try", "void", "volatile", "while","const","goto"};
	
	static Set<String> reservedWordSet = null;

	static {
		reservedWordSet = new HashSet<String>();
		for (String s : reservedWordList) {
			reservedWordSet.add(s);
		}
		System.out.println("all " + reservedWordSet.size() + " reserved words loaded");
	}
	
	public static String toCamelCase(String s,boolean firstCharUppercase){
		char [] cl = s.toCharArray();
		StringBuffer sb = new StringBuffer();
		boolean upper = firstCharUppercase;
		for(char ch : cl){
			switch(ch){
			case '_':
			case '?':
			case ';': // add anything to escape
				upper = true;
				break;
			default:
				sb.append((char)(upper?(ch>='a'&&ch<='z'?ch-'a'+'A':ch):ch));
				upper = false;
			}
		}
		return sb.toString();
	}
	
	public static String toCamelCase(String s){
		return toCamelCase(s, true);
	}

	public static String reservedWordEscape(String s) {
		if (s == null || "".equals(s.trim()))
			return null;
		return reservedWordSet.contains(s.trim()) ? ("_" + s) : s;
	}
	
	
	public static void main(String [] args){
		String s = "abstract";
		String s2 = "not_abstract";
		System.out.println(reservedWordEscape(s));
		System.out.println(reservedWordEscape(s2));
		System.out.println(toCamelCase(s2)); 
	}

}
