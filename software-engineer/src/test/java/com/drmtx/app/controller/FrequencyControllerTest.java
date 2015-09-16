package com.drmtx.app.controller;

import com.drmtx.app.domain.WordCount;
import com.drmtx.app.service.RedditWordCountService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class FrequencyControllerTest {

	FrequencyController frequencyController;

	@Mock
	private RedditWordCountService redditWordCountService;


	@Before
	public void setup(){
		frequencyController = new FrequencyController(redditWordCountService);
	}


	@Test
	public void shouldListWordCounts(){
		List<WordCount> wcs = ImmutableList.of(
				new WordCount("1", "abc", "oneword", 1L),
				new WordCount("2", "abc", "twoword", 1L));

		when(redditWordCountService.getList()).thenReturn(wcs);

		List<WordCount> returnedWcs = frequencyController.listWordCounts();
		assertEquals("should return the two wcs", 2, returnedWcs.size());
		assertEquals("should return the first wc", "1", returnedWcs.get(0).getId());
		assertEquals("should return the second wc", "2", returnedWcs.get(1).getId());
	}

	@Test
	public void shouldListWordCountsForRunid(){

		List<WordCount> wcs = ImmutableList.of(
				new WordCount("1", "abc", "oneword", 1L),
				new WordCount("2", "abc", "twoword", 1L));

		when(redditWordCountService.getListForRunid("abc", 2)).thenReturn(wcs);

		List<WordCount> returnedWcs = frequencyController.listWordCountsForRunId("abc", 2);

		verify(redditWordCountService, times(1)).getListForRunid("abc", 2);
		assertEquals(wcs.size(), returnedWcs.size());
	}


	@Test
	public void shouldCreateNewFrequency() throws IOException{

		when(redditWordCountService.getWordCount(any(URL.class))).thenReturn(Maps.newHashMap(ImmutableMap.of("one", 1L, "two", 2L)));

		String uuid = frequencyController.createNewFrequency("http://example.com").get("runid");
		verify(redditWordCountService, times(1)).saveAll(anyListOf(WordCount.class));

		assertEquals("returned runid should be a uuid", 36, uuid.length());
	}

}
