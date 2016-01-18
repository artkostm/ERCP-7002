package com.artkostm.core.akka.worldcount.model;

import java.io.Serializable;

public class Word implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private final String word;
    private Integer count;
    
    public Word(final String word, final Integer count)
    {
        this.word = word;
        this.count = count;
    }
    
    public Word(final String word)
    {
        this.word = word;
        count = 0;
    }

    public Integer getCount()
    {
        return count;
    }

    public void setCount(Integer count)
    {
        this.count = count;
    }

    public String getWord()
    {
        return word;
    }

    @Override
    public String toString()
    {
        return "Word [word=" + word + ", count=" + count + "]";
    }
}
