package com.anagrams.project.model;

import com.anagrams.project.util.Stats;

public class StatsResource implements Comparable<StatsResource> {

    private int minWordsLength;
    private int maxWordsLength;
    private int medianWordsLength;
    private int averageWordLength;

    public StatsResource(Stats stats){
        minWordsLength = stats.getMinWordsLength();
        maxWordsLength = stats.getMaxWordsLength();
        medianWordsLength = stats.getMedianWordsLength();
        averageWordLength = stats.getAverageWordLength();
    }

    @Override
    public int compareTo(StatsResource o) {
        if(minWordsLength == o.minWordsLength &&
                maxWordsLength == o.maxWordsLength &&
                medianWordsLength == o.medianWordsLength &&
                averageWordLength == o.averageWordLength )
            return 0;
        return -1;
    }


    public boolean equals(StatsResource o) {
        if (minWordsLength == o.minWordsLength &&
                maxWordsLength == o.maxWordsLength &&
                medianWordsLength == o.medianWordsLength &&
                averageWordLength == o.averageWordLength)
            return true;
        return false;
    }
}
