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

    //-------------------------------------------------------------------------------------------------------------------
    //                                          ADD
    //-------------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldReturn201ResponseWhenPopulatedDB() {
        when(dataloaderService.init()).thenReturn(true);
        ResponseEntity expectedResponse = new ResponseEntity<>("Dictionary file has been loaded in to the corpus DB", HttpStatus.CREATED);
        ResponseEntity actualResponse = anagramRestResource.populateWords();
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturn400ResponseWhenAlreadyPopulatedDB(){
        when(dataloaderService.init()).thenReturn(false);
        ResponseEntity expectedResponse =  new ResponseEntity<>("The DB is already filled up", HttpStatus.BAD_REQUEST);
        ResponseEntity actualResponse = anagramRestResource.populateWords();
        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    public void shouldReturn201ResponseAddedWordsIfTrue() {
        AnagramPost anagramPostMock = mock(AnagramPost.class);
        List<String> words = anagramPostMock.getWords();
        when(anagramService.addWordsAsAnagram(words)).thenReturn(true);
        ResponseEntity expectedResponse = new ResponseEntity<>("The new word(s) were added as anagrams", HttpStatus.CREATED);
        ResponseEntity actualResponse = anagramRestResource.addWordsAsAnagram(anagramPostMock);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturn201ResponseAddedWordsIfFalse() {
        AnagramPost anagramPostMock = mock(AnagramPost.class);
        List<String> words = anagramPostMock.getWords();
        when(anagramService.addWordsAsAnagram(words)).thenReturn(false);
        ResponseEntity expectedResponse = new ResponseEntity<>("The input word(s) have already been added", HttpStatus.CREATED);
        ResponseEntity actualResponse = anagramRestResource.addWordsAsAnagram(anagramPostMock);
        assertEquals(expectedResponse, actualResponse);
    }

    //-------------------------------------------------------------------------------------------------------------------
    //                                          FETCH
    //-------------------------------------------------------------------------------------------------------------------

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

    //Fetch Word as Anagram
    @Test
    public void shouldReturn200ResponseAndResultInTheBodyResponseWhenFetchForAnagramsOfExistingWord(){
        AnagramGet anagramGet = new AnagramGet(mock(List.class));
        when(anagramService.fetchAnagramsOfWord(anyString(),anyInt(),anyBoolean())).thenReturn(anagramGet);
        ResponseEntity expectedResponse = new ResponseEntity(anagramGet, HttpStatus.OK);
        ResponseEntity actualResponse = anagramRestResource.fetchAnagramsOfWord("aWord", 0, true);
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturn200ResponseAndResultInTheBodyResponseWhenFetchForAnagramsOfNonExistingWord(){
        List<String> list = new ArrayList<>();
        AnagramGet anagramGet = new AnagramGet(list);
        when(anagramService.fetchAnagramsOfWord(anyString(),anyInt(),anyBoolean())).thenReturn(anagramGet);
        ResponseEntity expectedResponse = new ResponseEntity(anagramGet, HttpStatus.OK);
        ResponseEntity actualResponse = anagramRestResource.fetchAnagramsOfWord("aWord", 0, true);
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    //Fetch Most Anagrams Words
    @Test
    public void shouldReturn200ResponseIfMostAnagramsDoesNotExist(){
        List<String> list = new ArrayList<>();
        AnagramGet anagramGet = new AnagramGet(list);
        when(anagramService.fetchMostAnagramsWords()).thenReturn(anagramGet);
        ResponseEntity expectedResponse = new ResponseEntity<>(anagramGet, HttpStatus.OK);
        ResponseEntity actualResponse = anagramRestResource.fetchMostAnagramsWords();
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturn200ResponseAndResultInTheBodyResponseIfMostAnagramsExist(){
        AnagramGet anagramGet = new AnagramGet(mock(List.class));
        when(anagramService.fetchMostAnagramsWords()).thenReturn(anagramGet);
        ResponseEntity expectedResponse = new ResponseEntity(anagramGet, HttpStatus.OK);
        ResponseEntity actualResponse = anagramRestResource.fetchMostAnagramsWords();
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    //Fetch Anagrams of Group Size
    @Test
    public void shouldReturn200ResponseIfAnagramsOfGroupSizeDoesNotExist(){
        List<String> list = new ArrayList<>();
        AnagramGet anagramGet = new AnagramGet(list);
        when(anagramService.fetchAnagramsOfGroupSize(anyString())).thenReturn(anagramGet);
        ResponseEntity expectedResponse = new ResponseEntity<>(anagramGet, HttpStatus.OK);
        ResponseEntity actualResponse = anagramRestResource.fetchAnagramsOfGroupSize("2");
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturnResultInTheBodyResponseIfAnagramsOfGroupSizeExist(){
        AnagramGet anagramGet = new AnagramGet(mock(List.class));
        when(anagramService.fetchAnagramsOfGroupSize(anyString())).thenReturn(anagramGet);
        ResponseEntity expectedResponse = new ResponseEntity(anagramGet, HttpStatus.OK);
        ResponseEntity actualResponse = anagramRestResource.fetchAnagramsOfGroupSize( "2" );
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    //Fetch Stats
    @Test
    public void shouldReturn200ResponseAndStatsInTheBody() {
        Stats stats = mock(Stats.class);
        when(statsService.calculateStats()).thenReturn(stats);
        ResponseEntity expectedResponse = new ResponseEntity(stats, HttpStatus.OK);
        ResponseEntity actualResponse = anagramRestResource.fetchStats();
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturn200ResponseIfStatsDoesNotExist() {
        Stats stats = new Stats();
        when(statsService.calculateStats()).thenReturn(stats);
        ResponseEntity expectedResponse = new ResponseEntity(stats, HttpStatus.OK);
        ResponseEntity actualResponse = anagramRestResource.fetchStats();
        assertEquals(expectedResponse, actualResponse);
    }

    //-------------------------------------------------------------------------------------------------------------------
    //                                          DELETE
    //-------------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldReturn204ResponseWhenDeletedAll(){
        ResponseEntity expectedResponse = new ResponseEntity<>("Deleted All Data successfully!", HttpStatus.NO_CONTENT);
        ResponseEntity actualResponse = anagramRestResource.deleteAll();
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturn204ResponseIfDeletedWordIsTrue(){
        when(anagramService.deleteWord(anyString())).thenReturn(true);
        ResponseEntity expectedResponse = new ResponseEntity<>("The input word was successfully deleted", HttpStatus.NO_CONTENT);
        ResponseEntity actualResponse = anagramRestResource.deleteWord("A word");
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturn204ResponseIfDeletedWordIsFalse(){
        when(anagramService.deleteWord(anyString())).thenReturn(false);
        ResponseEntity expectedResponse = new ResponseEntity<>("Unable to delete the input word", HttpStatus.BAD_REQUEST);
        ResponseEntity actualResponse = anagramRestResource.deleteWord("A word");
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturn204ResponseIfDeletedAnagramsOfWordIsTrue(){
        when(anagramService.deleteAnagramsOfWord(anyString())).thenReturn(true);
        ResponseEntity expectedResponse = new ResponseEntity<>("The anagrams of the input word were successfully deleted", HttpStatus.NO_CONTENT);
        ResponseEntity actualResponse = anagramRestResource.deleteAnagramsOfWord("A word");
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturn204ResponseIfDeletedAnagramsOfWordIsFalse(){
        when(anagramService.deleteAnagramsOfWord(anyString())).thenReturn(false);
        ResponseEntity expectedResponse = new ResponseEntity<>("Unable to delete the anagrams of the input word", HttpStatus.BAD_REQUEST);
        ResponseEntity actualResponse = anagramRestResource.deleteAnagramsOfWord("A word");
        assertEquals(expectedResponse, actualResponse);
    }

}