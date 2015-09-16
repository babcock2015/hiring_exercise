package com.drmtx.app.repository;

import com.drmtx.app.domain.WordCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import java.util.List;


public interface WordCountRepository extends JpaRepository<WordCount, String> {

	@Query("select wc from WordCount wc where wc.runid = ?1 order by wc.count desc")
		public List<WordCount> findWordCountsByRunidOrdered(String runid, Pageable page);

}
