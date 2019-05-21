package com.argcv.gpm.util.analyzer;

import java.io.IOException;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class KeywordsFilter extends org.apache.lucene.analysis.TokenFilter {
	// term attribute
	private CharTermAttribute cta = null;

	protected KeywordsFilter(TokenStream input) {
		super(input);
		cta = this.addAttribute(CharTermAttribute.class);
	}

	@Override
	public boolean incrementToken() throws IOException {
		// check if the information is available
		if (input == null)
			return false;
		//TODO what the matter ?
		try {
			if (!input.incrementToken())
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		/**
		 * no any filter
		 */
		// if (cta.toString().equals("家乡")) {
		// cta.setEmpty();
		// cta.append("故乡");
		// }

		String str = cta.toString();
		cta.setEmpty();
		cta.append(str.trim());

		return true;
	}

}
