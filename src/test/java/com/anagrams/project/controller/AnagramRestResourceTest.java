package com.anagrams.project.controller;

import com.anagrams.project.entity.Anagram;
import com.anagrams.project.model.AnagramGet;
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
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
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
        ResponseEntity expectedResponse = new ResponseEntity<>("Dictionary has been added to the corpus", HttpStatus.CREATED);
        ResponseEntity actualResponse = anagramRestResource.populateWords();
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void populate_words_should_return_400_response_if_false(){
        when(dataloaderService.init()).thenReturn(false);
        ResponseEntity expectedResponse =  new ResponseEntity<>("There was an error processing your request", HttpStatus.BAD_REQUEST);
        ResponseEntity actualResponse = anagramRestResource.populateWords();
        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    public void shouldReturnWordsAreAnagramsIfTrue() {
        AnagramPost anagramPostMock = mock(AnagramPost.class);
        List<String> words = anagramPostMock.getWords();
        when(anagramService.areAnagrams(words)).thenReturn(true);
        ResponseEntity expectedResponse =new ResponseEntity<>("This words are Anagrams between each others!", HttpStatus.OK);
        ResponseEntity actualResponse = anagramRestResource.areAnagrams(anagramPostMock);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturnWordsAreNotAnagramsIfFalse() {
        AnagramPost anagramPostMock = mock(AnagramPost.class);
        List<String> words = anagramPostMock.getWords();
        when(anagramService.areAnagrams(words)).thenReturn(false);
        ResponseEntity expectedResponse =new ResponseEntity<>("This words are not anagrams", HttpStatus.OK);
        ResponseEntity actualResponse = anagramRestResource.areAnagrams(anagramPostMock);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturn201ResponseAfterAddingWord() {
        AnagramPost anagramPostMock = mock(AnagramPost.class);
        List<String> words = anagramPostMock.getWords();
        when(anagramService.addWordsAsAnagram(words)).thenReturn(true);
        ResponseEntity expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.CREATED);
        ResponseEntity actualResponse = anagramRestResource.addWordsAsAnagram(anagramPostMock);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturn200ResponseIfThereAreNoAnagramsOfWord(){
        List<String> list = new ArrayList<>();
        AnagramGet anagramGet = new AnagramGet(list);
        when(anagramService.fetchAnagramsOfWord(anyString(),anyInt(),anyBoolean())).thenReturn(anagramGet);
        ResponseEntity expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.OK);
        ResponseEntity actualResponse = anagramRestResource.fetchAnagramsOfWord("aWord", 0, true);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturnResultInTheBodyResponseIfThereAreAnagramsOfWord(){
        AnagramGet anagramGet = new AnagramGet();
        anagramGet.setAnagrams(mock(List.class));
        when(anagramService.fetchAnagramsOfWord(anyString(),anyInt(),anyBoolean())).thenReturn(anagramGet);
        ResponseEntity expectedResponse = new ResponseEntity(anagramGet, HttpStatus.OK);
        ResponseEntity actualResponse = anagramRestResource.fetchAnagramsOfWord("aWord", 0, true);
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturnStatsIfStatsIsNull() {
        when(statsService.calculateStats()).thenReturn(null);
        ResponseEntity expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.NO_CONTENT);
        ResponseEntity actualResponse = anagramRestResource.fetchStats();
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturnStatsIfStatsIsNotNull() {
        Stats stats = mock(Stats.class);
        when(statsService.calculateStats()).thenReturn(stats);
        ResponseEntity expectedResponse = new ResponseEntity(stats, HttpStatus.OK);
        ResponseEntity actualResponse = anagramRestResource.fetchStats();
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturn200ResponseIfMostAnagramsDoesNotExist(){
        List<String> list = new ArrayList<>();
        AnagramGet anagramGet = new AnagramGet(list);
        when(anagramService.fetchMostAnagramsWords()).thenReturn(anagramGet);
        ResponseEntity expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.OK);
        ResponseEntity actualResponse = anagramRestResource.fetchMostAnagramsWords();
        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    public void shouldReturnResultInTheBodyResponseIfMostAnagramsExist(){
        AnagramGet anagramGet = new AnagramGet();
        anagramGet.setAnagrams(mock(List.class));
        when(anagramService.fetchMostAnagramsWords()).thenReturn(anagramGet);
        ResponseEntity expectedResponse = new ResponseEntity(anagramGet, HttpStatus.OK);
        ResponseEntity actualResponse = anagramRestResource.fetchMostAnagramsWords();
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturn200ResponseIfAnagramsOfGroupSizeDoesNotExist(){
        List<String> list = new ArrayList<>();
        AnagramGet anagramGet = new AnagramGet(list);
        when(anagramService.fetchAnagramsOfGroupSize(anyInt())).thenReturn(anagramGet);
        ResponseEntity expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.OK);
        ResponseEntity actualResponse = anagramRestResource.fetchAnagramsOfGroupSize("2");
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturnResultInTheBodyResponseIfAnagramsOfGroupSizeExist(){
        AnagramGet anagramGet = new AnagramGet();
        anagramGet.setAnagrams(mock(List.class));
        when(anagramService.fetchAnagramsOfGroupSize(anyInt())).thenReturn(anagramGet);
        ResponseEntity expectedResponse = new ResponseEntity(anagramGet, HttpStatus.OK);
        ResponseEntity actualResponse = anagramRestResource.fetchAnagramsOfGroupSize( "2" );
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    //What to do for testing for Numeric Exception?
    @Test
    public void shouldReturn400ResponseIfAnagramsOfGroupSizeIsNotNumber(){//Pending to fix
        List<String> list = new ArrayList<>();
        AnagramGet anagramGet = new AnagramGet(list);
        doThrow(new NumberFormatException()).when(anagramRestResource.fetchAnagramsOfGroupSize(anyString()));
        //when(anagramRestResource.fetchAnagramsOfGroupSize(anyString())).(new NumberFormatException() );
        ResponseEntity expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.BAD_REQUEST);
        ResponseEntity actualResponse = anagramRestResource.fetchAnagramsOfGroupSize("not a number");
        assertEquals(expectedResponse, actualResponse);
    }

    //What to do for testing DeleteALL()
    @Test
    public void shouldReturn204ResponseIfDeletedAll(){
        when(anagramService.deleteAll()).thenReturn(true);
        ResponseEntity expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.NO_CONTENT);
        ResponseEntity actualResponse = anagramRestResource.deleteAll();
        assertEquals(expectedResponse, actualResponse);
    }
    //What to do for testing DeleteALL()

    @Test
    public void shouldReturn500ResponseIfDeletedAll(){
        when(anagramService.deleteAll()).thenReturn(false);
        ResponseEntity expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity actualResponse = anagramRestResource.deleteAll();
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturn204ResponseIfDeletedWordIsTrue(){
        when(anagramService.deleteWord(anyString())).thenReturn(true);
        ResponseEntity expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.NO_CONTENT);
        ResponseEntity actualResponse = anagramRestResource.deleteWord("A word");
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturn204ResponseIfDeletedWordIsFalse(){
        when(anagramService.deleteWord(anyString())).thenReturn(false);
        ResponseEntity expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.BAD_REQUEST);
        ResponseEntity actualResponse = anagramRestResource.deleteWord("A word");
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturn204ResponseIfDeletedAnagramsOfWordIsTrue(){
        when(anagramService.deleteWord(anyString())).thenReturn(true);
        ResponseEntity expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.NO_CONTENT);
        ResponseEntity actualResponse = anagramRestResource.deleteWord("A word");
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturn204ResponseIfDeletedAnagramsOfWordIsFalse(){
        when(anagramService.deleteWord(anyString())).thenReturn(false);
        ResponseEntity expectedResponse = new ResponseEntity<>("Unexpected response code", HttpStatus.BAD_REQUEST);
        ResponseEntity actualResponse = anagramRestResource.deleteWord("A word");
        assertEquals(expectedResponse, actualResponse);
    }

}