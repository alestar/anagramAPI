package com.anagrams.project.exception;

public class IncorrectAnagramException extends Exception {

    private static final String INVALID_WORD_MESSAGE = "The word used to attempt to retrieve anagrams is invalid or missing";
    private String word;


    public IncorrectAnagramException(String word){
        this.word = word;
    }
    @Override
    public String getMessage(){
        return INVALID_WORD_MESSAGE + ": " + word;

    }


}
