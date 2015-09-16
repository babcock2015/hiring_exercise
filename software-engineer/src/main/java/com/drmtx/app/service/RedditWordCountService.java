package com.drmtx.app.service;

import java.util.Map;
import java.net.URL;
import java.io.IOException;
import java.util.List;
import com.drmtx.app.domain.WordCount;


public interface RedditWordCountService {
    Map<String, Long> getWordCount(URL url) throws IOException;
	WordCount save(WordCount wordCount);
	List<WordCount> saveAll(List<WordCount> wordCount);
	List<WordCount> getList();
	List<WordCount> getListForRunid(String runid, Integer limit);
}
