package com.anagrams.project.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnagramGet {

    @SerializedName("anagrams")
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
