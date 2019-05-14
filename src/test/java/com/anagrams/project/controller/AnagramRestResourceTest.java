package com.anagrams.project.controller;

import com.anagrams.project.service.AnagramService;
import com.anagrams.project.service.DataloaderService;
import com.anagrams.project.service.StatsService;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    public void should_Return200_Response_If_True() {
        when(dataloaderService.init()).thenReturn(true);
        ResponseEntity<String> expectedResponse =
                new ResponseEntity<>("Dictionary has been added to the corpus", HttpStatus.CREATED);
        ResponseEntity<String> actualResponse = anagramRestResource.populateWords();
        Assert.assertEquals(expectedResponse, actualResponse);

    }

    @Test
    public void should_Return403_Response_If_False(){

        when(dataloaderService.init()).thenReturn(false);
        ResponseEntity<String> expectedResponse =
                new ResponseEntity<>("There was an error processing your request", HttpStatus.BAD_REQUEST);
        ResponseEntity<String> actualResponse = anagramRestResource.populateWords();
        Assert.assertEquals(actualResponse, expectedResponse);

    }

}