package com.anagrams.project.service;

import com.anagrams.project.entity.Anagram;
import com.anagrams.project.model.Stats;
import com.anagrams.project.repository.AnagramRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.*;

//@RunWith(MockitoJUnitRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class AnagramServiceTest {
    private final String TOKEN_FOR_LENGTH_4 = "ader";
    private final String TOKEN_FOR_LENGTH_5 = "admno";

    @MockBean
    private DataloaderService mockDataloaderService;
    @MockBean
    private AnagramRepository mockAnagramRepository;

    @Autowired
    private AnagramService testAnagramService;


    private List<String> testWordsList4;
    private List<String> testWordsList5;
    
    private Set<String> testWordsSet4;
    private Set<String> testWordsSet5;
    private Set<String> testTokensSet4;
    private Set<String> testTokensSet5;

    private Stats testStats;


    @Before
    public void setUp(){
        //MockitoAnnotations.initMocks(this);
        //testAnagramService= new AnagramService();
        //mockDataloaderService = new DataloaderService();
        mockDataloaderService.init();

        testWordsList4 = new ArrayList<>(Arrays.asList("read","dear","dare","ared","daer"));
        testWordsList5 = new ArrayList<>(Arrays.asList("monad","nomad","Damon"));

        testWordsSet4 = new HashSet<>( Arrays.asList("read","dear","dare","ared","daer"));
        testWordsSet5 = new HashSet<>( Arrays.asList("monad","nomad","Damon"));
        testTokensSet4 = new HashSet<>( Arrays.asList(TOKEN_FOR_LENGTH_4));
        testTokensSet5 = new HashSet<>( Arrays.asList(TOKEN_FOR_LENGTH_5));

        testStats = new Stats();
        testStats.setMinWordsLength(4);
        testStats.setMaxWordsLength(5);
        testStats.setMedianWordsLength(5);
        testStats.setAverageWordLength(4);
    }

    @Test
    public void generateAnagramTokenShouldReturnAValidToken(){
        assertEquals("", testAnagramService.generateAnagramToken(""));
        assertEquals("", testAnagramService.generateAnagramToken(null));
        assertEquals(TOKEN_FOR_LENGTH_4, testAnagramService.generateAnagramToken("read"));
        assertEquals(TOKEN_FOR_LENGTH_5, testAnagramService.generateAnagramToken("nomad"));
    }

    @Test
    public void testAddSingleListOfWordsAsAnagramShouldPopulateMaps() {
        try {
            //mockDataloaderService.init();
            testAnagramService.addWordsAsAnagram(testWordsList4);
            Anagram testAnagram = mockAnagramRepository.findByToken(TOKEN_FOR_LENGTH_4);
            Set<String> wordList = new HashSet<>(Arrays.asList(testAnagram.getWords().split(", ")));
            assertNotNull(mockAnagramRepository.findByToken(TOKEN_FOR_LENGTH_4));
            assertEquals(wordList,testWordsList4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/*
    @Test
    public void testAddMultipleListOfWordsAsAnagramShouldPopulateMaps() {
        try {
            List<String> combineList = new ArrayList<>();
            combineList.addAll(testWordsList4);
            combineList.addAll(testWordsList5);
            testAnagramService = new AnagramService();
            testAnagramService.populateDatabase(combineList);
            assertEquals(testLengthToTokensMap,testAnagramService.getLengthToTokensMap());
            assertEquals( testTokenToWordsMap,testAnagramService.getTokenToWordsMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFetchAnagramsOfWordNoParamsShouldReturnSetOfAnagrams() {
        try {
            testAnagramService= new AnagramService();
            Set<String> setWords1 = new HashSet<>(Arrays.asList("dear","dare","ared","daer"));//Set without "read"
            Set<String> setWords2 = new HashSet<>(Arrays.asList("read","dare","ared","daer"));//Set without "dear"
            testAnagramService.populateDatabase(testWordsList4);

            assertEquals(setWords1,testAnagramService.fetchAnagramsOfWord("read"));
            assertEquals(setWords2,testAnagramService.fetchAnagramsOfWord("dear"));
            assertNotEquals(setWords1,testAnagramService.fetchAnagramsOfWord("zwyq"));
            assertEquals(new HashSet<>(),testAnagramService.fetchAnagramsOfWord("zwyq"));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFetchAnagramsOfWordWithLimitSetShouldReturnSetOfAnagramLimited() {
        try {
            Set<String> setWords1 = new HashSet<>(Arrays.asList("daer"));
            Set<String> setWords2 = new HashSet<>(Arrays.asList("read"));
            testAnagramService = new AnagramService();
            testAnagramService.populateDatabase(testWordsList4);

            assertEquals(setWords1,testAnagramService.fetchAnagramsOfWord("read",1,true));
            assertEquals(setWords2,testAnagramService.fetchAnagramsOfWord("dear",1,true));
            assertEquals(new HashSet<>(),testAnagramService.fetchAnagramsOfWord("zwyq", 1, true));
            assertNotEquals(setWords1,testAnagramService.fetchAnagramsOfWord("zwyq",1, true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFetchAnagramsOfWordWithPermitPNSetNoLimitShouldReturnSetAnagramAccordingly() {
        try {
            List <String> words = new ArrayList<>( Arrays.asList("monad","nomad","Damon"));
            Set<String> setWords1 = new HashSet<>(Arrays.asList("nomad"));
            Set<String> setWords2 = new HashSet<>(Arrays.asList("monad"));
            Set<String> setWords3 = new HashSet<>(Arrays.asList("monad","Damon"));
            Set<String> setWords4 = new HashSet<>(Arrays.asList("monad","nomad"));

            testAnagramService = new AnagramService();
            testAnagramService.populateDatabase(testWordsList5);

            assertEquals(setWords1,testAnagramService.fetchAnagramsOfWord("monad",0,false));
            assertEquals(setWords2,testAnagramService.fetchAnagramsOfWord("nomad",0,false));
            assertEquals(setWords3,testAnagramService.fetchAnagramsOfWord("nomad",0,true));
            assertEquals(setWords4,testAnagramService.fetchAnagramsOfWord("Damon",0,true));
            assertEquals(setWords4,testAnagramService.fetchAnagramsOfWord("Damon",0,false));
            assertEquals(new HashSet<>(),testAnagramService.fetchAnagramsOfWord("zwyq", 0, true));
            assertNotEquals(setWords1,testAnagramService.fetchAnagramsOfWord("zwyq",0, true));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void areAnagrams() {

        try {
            List<String> combineList = new ArrayList<>();
            combineList.addAll(testWordsList4);
            combineList.addAll(testWordsList5);
            testAnagramService = new AnagramService();
            assertFalse(testAnagramService.areAnagrams(combineList));//Combined List are not anagrams between each other
            assertFalse(testAnagramService.areAnagrams(new ArrayList<>()));
            assertFalse(testAnagramService.areAnagrams(null));
            assertTrue(testAnagramService.areAnagrams(testWordsList4));
            assertTrue(testAnagramService.areAnagrams(testWordsList5));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void fetchStatsResource() {
        try {
            List<String> combineList = new ArrayList<>();
            combineList.addAll(testWordsList4);
            combineList.addAll(testWordsList5);
            testAnagramService = new AnagramService();
            testAnagramService.populateDatabase(combineList);
            Stats stats= testAnagramService.getStats();
            stats.calculateStats(testLengthToTokensMap.keySet());
            assertTrue(testStatsResource.equals(testAnagramService.fetchStatsResource()));
            assertTrue(testStats.equals(stats));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void fetchMostAnagramsWords() {
        try {
            List<String> combineList = new ArrayList<>();
            combineList.addAll(testWordsList4);
            combineList.addAll(testWordsList5);
            testAnagramService = new AnagramService();
            testAnagramService.populateDatabase(combineList);
            assertEquals(testWordsSet4,testAnagramService.fetchMostAnagramsWords());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fetchAnagramGroupOfSize() {
        try {
            List<String> combineList = new ArrayList<>();
            combineList.addAll(testWordsList4);
            combineList.addAll(testWordsList5);
            testAnagramService = new AnagramService();
            testAnagramService.populateDatabase(combineList);
            assertEquals(testWordsSet4,testAnagramService.fetchAnagramGroupOfSize(4));
            assertEquals(testWordsSet5,testAnagramService.fetchAnagramGroupOfSize(5));
            assertEquals(new HashSet<>(),testAnagramService.fetchAnagramGroupOfSize(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteWord() {
        try {
            testAnagramService= new AnagramService();
            testAnagramService.populateDatabase(testWordsList4);
            assertEquals(testWordsSet4, testAnagramService.getTokenToWordsMap().get(TOKEN_FOR_LENGTH_4));
            assertTrue(testAnagramService.deleteWord("read"));
            testWordsSet4.remove("read");
            assertEquals(testWordsSet4, testAnagramService.getTokenToWordsMap().get(TOKEN_FOR_LENGTH_4));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void deleteAllAnagramsOfWord() {
        try {
            testAnagramService= new AnagramService();
            testAnagramService.populateDatabase(testWordsList4);//First add the words
            assertEquals(testWordsSet4, testAnagramService.getTokenToWordsMap().get(TOKEN_FOR_LENGTH_4));//Make sure those words are in the map
            assertTrue(testAnagramService.deleteAllAnagramsOfWord("read"));//Then, proceed to remove the anagrams of the word
            assertNull(testAnagramService.getTokenToWordsMap().get(TOKEN_FOR_LENGTH_4));//Nothing should be returning for that token key
            assertFalse( testAnagramService.getLengthToTokensMap().get(4).contains(TOKEN_FOR_LENGTH_4));// That token is no longer in the other map
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteALL() {
        try {
            List<String> combineList = new ArrayList<>();
            combineList.addAll(testWordsList4);
            combineList.addAll(testWordsList5);
            testAnagramService = new AnagramService();
            testAnagramService.populateDatabase(combineList);//Add anagrams
            assertEquals(testWordsSet4,testAnagramService.getTokenToWordsMap().get(TOKEN_FOR_LENGTH_4));//Make sure the map is filled
            testAnagramService.deleteALL();// Clear Maps and Stats
            assertTrue(testAnagramService.getTokenToWordsMap().isEmpty());//Make sure both maps are empty
            assertTrue(testAnagramService.getLengthToTokensMap().isEmpty());

            assertEquals(0,testAnagramService.getStats().getTotalWords());//Make sure Stats is set to default
            assertEquals(0,testAnagramService.getStats().getMostAnagramsCounter());
            assertEquals(0,testAnagramService.getStats().getSumWordLengths());
            assertNull(testAnagramService.getStats().getMostAnagramsToken());
            assertEquals(-1,testAnagramService.getStats().getAverageWordLength());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

}
