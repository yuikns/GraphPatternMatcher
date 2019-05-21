package com.argcv.gpm.service;

import java.io.InputStream;
import java.util.List;

public interface ImageSearchService {
	public abstract List<String> query(InputStream inputStream, int maxNum);
}