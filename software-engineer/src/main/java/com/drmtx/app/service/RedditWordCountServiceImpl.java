package com.drmtx.app.service;

import java.util.Map;
import java.net.URL;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.io.IOException;
import com.drmtx.app.repository.WordCountRepository;
import com.drmtx.app.domain.WordCount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RedditWordCountServiceImpl implements RedditWordCountService {

	private final WordCountRepository repository;

	@Autowired
	public RedditWordCountServiceImpl(final WordCountRepository repository){
		this.repository = repository;
	}


	//This takes a reddit .json url and traverses the payload for body fields, which are broken into words and counted.
    public Map<String, Long> getWordCount(final URL url) throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		//find all body fields in payload
		List<String> commentStrings = mapper.readTree(url).findValuesAsText("body");

		                               //flatmap all comment words to one stream
		Map<String, Long> counts  = commentStrings.stream().flatMap(comment ->  
																	//split on boundries and lowercase
																	Arrays.asList(comment.replace("'", "").split("\\W+"))
																	.stream()
																	.map(s -> s.toLowerCase()))
			.collect(Collectors.groupingBy(o -> o, Collectors.counting()));
		//finally group words and count
		return counts;
	}



    @Override
    @Transactional
    public WordCount save(final WordCount wordCount) {
        return repository.save(wordCount);
    }

    @Override
    @Transactional
    public List<WordCount> saveAll(final List<WordCount> wordCounts) {
		for(WordCount wc : wordCounts){
			repository.save(wc);
		}
		return wordCounts;
    }

    @Override
    @Transactional(readOnly = true)
	public List<WordCount> getList(){
		return repository.findAll();
	}

	@Override
    @Transactional(readOnly = true)
	public List<WordCount> getListForRunid(String runid, Integer limit){
		return repository.findWordCountsByRunidOrdered(runid, new PageRequest(0, limit));
	}

}
