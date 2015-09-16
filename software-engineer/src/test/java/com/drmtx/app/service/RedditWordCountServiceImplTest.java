package com.drmtx.app.service;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.net.MalformedURLException;
import java.io.IOException;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.io.ByteArrayInputStream;
import org.mockito.Mock;
import com.drmtx.app.repository.WordCountRepository;
import com.drmtx.app.domain.WordCount;
import org.springframework.data.domain.Pageable;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RedditWordCountServiceImplTest {

	@Mock
	private WordCountRepository wordCountRepository;

	private RedditWordCountService redditWordCountService;

	//an abbreviated sample of what the reddit api returns
	private String redditData = "[" + 
	"	{\"data\": {\"children\": [{\"data\": {\"id\": \"32pj67\",\"num_comments\": 64,\"subreddit\": \"java\",\"subreddit_id\": \"t5_2qhd7\"}, \"kind\": \"t3\"}],\"modhash\": \"lkh5g62hmh79a356a49dadc248af6f96dbcc5f181200ee27d9\"},\"kind\": \"Listing\"}," + 
	"	{" +
	"        \"data\": {" + 
	"            \"children\": [" +
	"                {" + 
	"                    \"data\": {\"author\": \"seanr8\",\"body\": \"one two Awesome find but the return type of sandwich being void makes me so angry...\",\"id\": \"cqdex42\",\"parent_id\": \"t3_32pj67\", " +
	"                        \"replies\": { " +
	"                            \"data\": { " + 
	"                                \"children\": [" +
	"                                    {" +
	"                                        \"data\": {\"author\": \"deleted\",\"body\": \"two no way im getting coffee at a place whose sign doesnt even compile\",\"id\": \"cqdgead\"}," +
	"                                        \"kind\": \"t1\"" +
	"                                    }" +
	"                                ]," +
	"                                \"modhash\": \"lkh5g62hmh79a356a49dadc248af6f96dbcc5f181200ee27d9\"" +
	"                            }," +
	"                            \"kind\": \"Listing\"" +
	"                        }," +
	"                        \"score\": 132," +
	"                        \"subreddit_id\": \"t5_2qhd7\"" +
	"                    }," +
	"                    \"kind\": \"t1\"" +
	"                }" +
	"            ]," +
	"            \"modhash\": \"lkh5g62hmh79a356a49dadc248af6f96dbcc5f181200ee27d9\"" +
	"        }," +
	"        \"kind\": \"Listing\"" +
	"    }" +
	"]";
	

	@Before
	public void setUp() {
		redditWordCountService = new RedditWordCountServiceImpl(wordCountRepository);
	}



	@Test
	public void shouldSaveWordCount() {
		WordCount wordCount =  new WordCount("1", "abc", "theword", 1L);
		when(wordCountRepository.save(any(WordCount.class))).thenReturn(wordCount);

		WordCount returnedWordCount = redditWordCountService.save(wordCount);
		verify(wordCountRepository, times(1)).save(wordCount);
		assertEquals("saved object should be the same", wordCount, returnedWordCount);
	}

	@Test
	public void shouldSaveAllWordCounts() {

		WordCount savedWc = new WordCount("1", "abc", "savedword", 1L);
		List<WordCount> wcs = new ArrayList<WordCount>();
		wcs.add(savedWc);
		wcs.add(savedWc);


		when(wordCountRepository.save(any(WordCount.class))).thenReturn(savedWc);

		List<WordCount> returnedWordCount = redditWordCountService.saveAll(wcs);
		verify(wordCountRepository, times(2)).save(savedWc);
		assertEquals("saved object should be the same", returnedWordCount.get(0).getWord(), "savedword");
		assertEquals("saved object should be the same", returnedWordCount.get(0).getWord(), "savedword");
	}


	
    @Test
    public void shouldGetWordCount() throws MalformedURLException, IOException {

		URLConnection mockUrlCon = mock(URLConnection.class);
		ByteArrayInputStream is = new ByteArrayInputStream(redditData.getBytes("UTF-8"));
		doReturn(is).when(mockUrlCon).getInputStream();

		URLStreamHandler stubUrlHandler = new URLStreamHandler() {
				protected URLConnection openConnection(URL u) throws IOException {
					return mockUrlCon;
				}            
			};

		Map<String, Long> counts = redditWordCountService.getWordCount(new URL("https", "reddit.com", 443, "/r/java/comments/32pj67/java_reference_in_gta_v_beautiful/.json", stubUrlHandler));

        assertEquals("one occurs once", counts.get("one"), new Long(1L));
		assertEquals("two occurs twice", counts.get("two"), new Long(2L));
    }

	@Test
	public void shouldGetListForRunid(){

		List<WordCount> wcs = new ArrayList<WordCount>();
		wcs.add(new WordCount("1", "abc", "oneword", 1l));
		wcs.add(new WordCount("2", "abc", "twoword", 1l));

        when(wordCountRepository.findWordCountsByRunidOrdered(any(String.class), any(Pageable.class))).thenReturn(wcs);

		List<WordCount> returnedWcs = redditWordCountService.getListForRunid("abc", 2);
		assertEquals("should be first wc", returnedWcs.get(0).getId(), wcs.get(0).getId());
		assertEquals("should be second wc", returnedWcs.get(1).getId(), wcs.get(1).getId());
	}
	
	
}
