package com.anagrams.project.util;

import java.util.Set;
import java.util.TreeSet;

/**
 * CLass used to storage stats from the word corpus
 */
public class Stats {

    private int totalWords = -1;
    private int sumWordLengths = -1;
    private int mostAnagramsCounter = -1;
    private String mostAnagramsToken;

    private int minWordsLength = -1;
    private int maxWordsLength = -1;
    private int medianWordsLength = -1;
    private int averageWordLength = -1;

    public Stats(){}

    public int getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(int totalWords) {
        this.totalWords = totalWords;
    }

    public void incrementTotalWords(){
        totalWords++;
    }

    public int getSumWordLengths() {
        return sumWordLengths;
    }

    public void setSumWordLengths(int sumWordLengths) {
        this.sumWordLengths = sumWordLengths;
    }

    public void addToSumWordLengths(int lenght){

        sumWordLengths+= lenght;
    }

    public int getMostAnagramsCounter() {
        return mostAnagramsCounter;
    }

    public void setMostAnagramsCounter(int mostAnagramWordsCounter) {
        this.mostAnagramsCounter = mostAnagramWordsCounter;
    }

    public void updateMostAnagramWords(int size, String token){
        if(size > mostAnagramsCounter) {
            mostAnagramsCounter = size;
            mostAnagramsToken = token;
        }
    }

    public String getMostAnagramsToken() {
        return mostAnagramsToken;
    }

    public void setMostAnagramsToken(String mostAnagramsToken) {
        this.mostAnagramsToken = mostAnagramsToken;
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

    public void calculateStats (Set<Integer> lengths){
        Set<Integer> sorted = new TreeSet<>(lengths);// To order ascending the set of lengths
        //Integer[] arr = sorted.toArray(new Integer[0]);
        int[] arr= sorted.stream().mapToInt(Integer::intValue).toArray();
        minWordsLength = arr[0];
        maxWordsLength = arr[arr.length - 1];
        medianWordsLength = arr.length / 2;
        averageWordLength= (sumWordLengths / arr.length);

    }

}
