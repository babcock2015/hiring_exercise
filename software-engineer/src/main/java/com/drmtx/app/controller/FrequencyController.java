package com.drmtx.app.controller;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.drmtx.app.service.RedditWordCountService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.drmtx.app.domain.WordCount;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.net.URL;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FrequencyController {

	private final RedditWordCountService redditWordCountService;

	@Autowired
	public FrequencyController(RedditWordCountService redditWordCountService){
		this.redditWordCountService = redditWordCountService;
	}


	// A bad idea in general to just return all the records, but in this case of a small demo project it's ok
	@RequestMapping(value = "frequency/all", method=RequestMethod.GET)
	public List<WordCount> listWordCounts(){
		return redditWordCountService.getList();
	}

	//return the word counts for a run in order with a limit
	@RequestMapping(value = "frequency/{runid}", method=RequestMethod.GET)
	public List<WordCount> listWordCountsForRunId(@PathVariable("runid") String runid, @RequestParam("count") Integer limit){
		return redditWordCountService.getListForRunid(runid, limit);
	}

	//analyze and store a new reddit comment thread
	@RequestMapping(value = "frequency/new", method=RequestMethod.POST)
	public Map<String, String> createNewFrequency(@RequestParam("url") String url) throws IOException{
		Map<String, Long> wordCountMap = null;

		wordCountMap = redditWordCountService.getWordCount(new URL(url));
		String runid= UUID.randomUUID().toString();
		
		List<WordCount> wcs =  new ArrayList<WordCount>();
		for(Map.Entry<String, Long> wordEntry : wordCountMap.entrySet()){
			wcs.add(new WordCount(null, runid, wordEntry.getKey(), wordEntry.getValue()));
		}
		redditWordCountService.saveAll(wcs);
		
		return Maps.newHashMap(ImmutableMap.of("runid", runid));
		
	}


}
