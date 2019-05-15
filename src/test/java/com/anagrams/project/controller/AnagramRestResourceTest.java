package com.anagrams.project.controller;

import com.anagrams.project.model.AnagramPost;
import com.anagrams.project.model.Stats;
import com.anagrams.project.service.AnagramService;
import com.anagrams.project.service.DataloaderService;
import com.anagrams.project.service.StatsService;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class AnagramRestResourceTest {

    @Mock
    DataloaderService dataloaderService;

    @Mock
    StatsService statsService;

    @Mock
    AnagramService anagramService;

    @InjectMocks
    AnagramRestResource anagramRestResource;

    @Test
    public void populate_words_should_return_201_response_if_true() {
        when(dataloaderService.init()).thenReturn(true);
        ResponseEntity<String> expectedResponse =
                new ResponseEntity<>("Dictionary has been added to the corpus", HttpStatus.CREATED);
        ResponseEntity<String> actualResponse = anagramRestResource.populateWords();
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void populate_words_should_return_400_response_if_false(){
        when(dataloaderService.init()).thenReturn(false);
        ResponseEntity<String> expectedResponse =
                new ResponseEntity<>("There was an error processing your request", HttpStatus.BAD_REQUEST);
        ResponseEntity<String> actualResponse = anagramRestResource.populateWords();
        Assert.assertEquals(actualResponse, expectedResponse);
    }

    @Test
    public void are_anagrams_should_return_200_response_if_true() {
        when(anagramService.areAnagrams(new ArrayList<>())).thenReturn(true);
        ResponseEntity<String> expectedResponse =new ResponseEntity<>("This words are Anagrams between each others!", HttpStatus.OK);
        ArrayList<String> wordList = new ArrayList<>( Arrays.asList("read","dear"));
        ResponseEntity<String> actualResponse = anagramRestResource.areAnagrams( new AnagramPost(wordList) );
        Assert.assertEquals(expectedResponse, actualResponse);
    }
    @Test
    public void are_anagrams_should_return_400_response_if_false() {
        when(anagramService.areAnagrams(new ArrayList<>())).thenReturn(false);
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("This words are not anagrams", HttpStatus.BAD_REQUEST);
        ArrayList<String> wordList = new ArrayList<>( Arrays.asList("read","dear"));
        ResponseEntity<String> actualResponse = anagramRestResource.areAnagrams( new AnagramPost(wordList) );
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void add_words_as_anagrams_should_return_201_response_if_true() {
        when(anagramService.addWordsAsAnagram(new ArrayList<>())).thenReturn(true);
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.CREATED);
        ArrayList<String> wordList = new ArrayList<>( Arrays.asList("read","dear"));
        ResponseEntity<String> actualResponse = anagramRestResource.addWordsAsAnagram( new AnagramPost(wordList) );
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void add_words_as_anagrams_should_return_201_response_if_false() {
        when(anagramService.addWordsAsAnagram(new ArrayList<>())).thenReturn(false);
        ResponseEntity<String> expectedResponse =
                new ResponseEntity<>("Unexpected response code", HttpStatus.CREATED);
        ArrayList<String> wordList = new ArrayList<>( Arrays.asList("read","dear"));
        ResponseEntity<String> actualResponse = anagramRestResource.addWordsAsAnagram( new AnagramPost(wordList) );
        Assert.assertEquals(expectedResponse, actualResponse);
    }
    @Test
    public void fetch_anagrams_of_a_word_should_return_200_response_if_result_null() {
        when(anagramService.fetchAnagramsOfWord("",0,true)).thenReturn(null);
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.OK);
        ResponseEntity<String> actualResponse = anagramRestResource.fetchAnagramsOfWord("read",0,true );
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void fetch_anagrams_of_a_word_should_return_200_and_json_response_if_result_not_null() {
        when(anagramService.fetchAnagramsOfWord("",0,true)).thenReturn(new ArrayList<>());
        ResponseEntity<String> expectedResponse = new ResponseEntity<>( new Gson().toJson(""), HttpStatus.OK);
        ResponseEntity<String> actualResponse = anagramRestResource.fetchAnagramsOfWord("read",0,true );
        Assert.assertEquals(expectedResponse, actualResponse);
    }
    @Test
    public void fetch_stats_should_return_204_response_if_result_null() {
        when(statsService.calculateStats()).thenReturn(null);
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.NO_CONTENT);
        ResponseEntity<String> actualResponse = anagramRestResource.fetchStats();
        Assert.assertEquals(expectedResponse, actualResponse);
    }
    @Test
    public void fetch_stats_should_return_200_and_json_response_if_result_not_null() {
        when(statsService.calculateStats()).thenReturn(new Stats());
        ResponseEntity<String> expectedResponse = new ResponseEntity<>(new Gson().toJson(""), HttpStatus.OK);
        ResponseEntity<String> actualResponse = anagramRestResource.fetchStats();
        Assert.assertEquals(expectedResponse, actualResponse);
    }
    @Test
    public void fetch_most_anagrams_words_should_return_204_if_result_null() {
        when(anagramService.fetchMostAnagramsWords()).thenReturn(null);
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.NO_CONTENT);
        ResponseEntity<String> actualResponse = anagramRestResource.fetchMostAnagramsWords();
        Assert.assertEquals(expectedResponse, actualResponse);
    }
    @Test
    public void fetch_most_anagrams_words_should_return_200_and_json_response_if_result_not_null() {
        when(anagramService.fetchMostAnagramsWords()).thenReturn(new ArrayList<>());
        ResponseEntity<String> expectedResponse = new ResponseEntity<>(new Gson().toJson(""), HttpStatus.OK);
        ResponseEntity<String> actualResponse = anagramRestResource.fetchMostAnagramsWords();
        Assert.assertEquals(expectedResponse, actualResponse);
    }
    @Test
    public void fetch_anagrams_of_group_size_should_return_204_if_result_null() {
        when(anagramService.fetchAnagramGroupOfSize(0)).thenReturn(null);
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.NO_CONTENT);
        ResponseEntity<String> actualResponse = anagramRestResource.fetchAnagramsGroupOfSize("");
        Assert.assertEquals(expectedResponse, actualResponse);
    }
    @Test
    public void fetch_anagrams_of_group_size_should_return_200_and_json_response_if_result_not_null() {
        when(anagramService.fetchAnagramGroupOfSize(1)).thenReturn(new ArrayList<>());
        ResponseEntity<String> expectedResponse = new ResponseEntity<>(new Gson().toJson(""), HttpStatus.OK);
        ResponseEntity<String> actualResponse = anagramRestResource.fetchAnagramsGroupOfSize("1");
        Assert.assertEquals(expectedResponse, actualResponse);
    }
    @Test
    public void fetch_anagrams_of_group_size_should_throw_exception_and_400_when_invalid_number_is_passed() {
        when(anagramService.fetchAnagramGroupOfSize(Integer.valueOf("aa"))).thenThrow(new NumberFormatException());
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.BAD_REQUEST);
        ResponseEntity<String> actualResponse = anagramRestResource.fetchAnagramsGroupOfSize("aa");
        Assert.assertEquals(expectedResponse, actualResponse);
    }
    @Test
    public void delete_all_should_return_204_when_no_exception_captured() {
        when(anagramService.deleteAll()).thenReturn(true);
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.NO_CONTENT);
        ResponseEntity<String> actualResponse = anagramRestResource.deleteAll();
        Assert.assertEquals(expectedResponse, actualResponse);
    }
    @Test
    public void delete_all_should_throw_exception_and_500_when_exception_captured() {
        when(anagramService.deleteAll()).thenThrow(new Exception());
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity<String> actualResponse = anagramRestResource.deleteAll();
        Assert.assertEquals(expectedResponse, actualResponse);
    }
    @Test
    public void delete_word_should_return_204_if_response_is_true() {
        when(anagramService.deleteWord("read")).thenReturn(true);
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.NO_CONTENT);
        ResponseEntity<String> actualResponse = anagramRestResource.deleteWord("read");
        Assert.assertEquals(expectedResponse, actualResponse);
    }
    @Test
    public void delete_word_should_return_400_if_response_is_false() {
        when(anagramService.deleteWord("")).thenReturn(false);
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.BAD_REQUEST);
        ResponseEntity<String> actualResponse = anagramRestResource.deleteWord("");
        Assert.assertEquals(expectedResponse, actualResponse);
    }
    @Test
    public void delete_anagrams_of_word_should_return_200_if_response_is_true() {
        when(anagramService.deleteAnagramsOfWord("read")).thenReturn(true);
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.OK);
        ResponseEntity<String> actualResponse = anagramRestResource.deleteAnagramsOfWord("read");
        Assert.assertEquals(expectedResponse, actualResponse);
    }
    @Test
    public void delete_anagrams_of_word_should_return_400_if_response_is_false() {
        when(anagramService.deleteAnagramsOfWord("")).thenReturn(false);
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.BAD_REQUEST);
        ResponseEntity<String> actualResponse = anagramRestResource.deleteAnagramsOfWord("");
        Assert.assertEquals(expectedResponse, actualResponse);
    }
}