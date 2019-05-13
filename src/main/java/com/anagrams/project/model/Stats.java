package com.anagrams.project.model;


public class Stats implements Comparable<Stats> {

    private int minWordsLength;
    private int maxWordsLength;
    private int medianWordsLength;
    private int averageWordLength;

    public Stats() {
    }

    public int getMinWordsLength() {
        return minWordsLength;
    }

    public void setMinWordsLength(int minWordsLength) {
        this.minWordsLength = minWordsLength;
    }

    public int getMaxWordsLength() {
        return maxWordsLength;
    }

    public void setMaxWordsLength(int maxWordsLength) {
        this.maxWordsLength = maxWordsLength;
    }

    public int getMedianWordsLength() {
        return medianWordsLength;
    }

    public void setMedianWordsLength(int medianWordsLength) {
        this.medianWordsLength = medianWordsLength;
    }

    public int getAverageWordLength() {
        return averageWordLength;
    }

    public void setAverageWordLength(int averageWordLength) {
        this.averageWordLength = averageWordLength;
    }

    @Override
    public int compareTo(Stats o) {
        if(minWordsLength == o.minWordsLength &&
                maxWordsLength == o.maxWordsLength &&
                medianWordsLength == o.medianWordsLength &&
                averageWordLength == o.averageWordLength )
            return 0;
        return -1;
    }


    public boolean equals(Stats o) {
        if (minWordsLength == o.minWordsLength &&
                maxWordsLength == o.maxWordsLength &&
                medianWordsLength == o.medianWordsLength &&
                averageWordLength == o.averageWordLength)
            return true;
        return false;
    }
}