package com.anagrams.project.model;

import java.util.List;

public class AnagramGet {
    private List<String> anagrams;

    public AnagramGet(){

    }
    public AnagramGet(List<String> anagrams) {
        this.anagrams = anagrams;
    }

    public List<String> getAnagrams() {
        return anagrams;
    }

    public void setAnagrams(List<String> anagrams) {
        this.anagrams = anagrams;
    }
}
