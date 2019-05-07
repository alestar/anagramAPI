package com.anagrams.project.model;

import com.anagrams.project.util.Stats;

public class StatsResource {

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
}
