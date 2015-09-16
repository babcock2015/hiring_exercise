package com.drmtx.app.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Table(uniqueConstraints=@UniqueConstraint(columnNames={"runid", "word"}))
@Entity
public class WordCount {

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private String id;


    @Column(name = "runid", nullable = false, updatable = false)
    private String runid;


    @Column(name = "word", nullable = false)
    private String word;


    @Column(name = "count", nullable = false)
    private Long count;

    WordCount() {
	}

    public WordCount(final String id, final String runid, final String word, final Long count) {
        this.id = id;
        this.runid = runid;
        this.word = word;
        this.count = count;
    }

	@JsonIgnore
    public String getId() {
        return id;
    }

	@JsonIgnore
    public String getRunid() {
        return runid;
    }

    public void setRunid(String runId) {
        this.runid = runId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

}
