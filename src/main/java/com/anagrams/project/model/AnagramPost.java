package com.anagrams.project.model;

import java.util.List;


public class AnagramPost {
    private List<String> words;

    public AnagramPost(){

    }

    public AnagramPost(List<String> words) {
        this.words = words;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }
}
