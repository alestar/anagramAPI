package com.anagrams.project.service;

import com.anagrams.project.entity.Anagram;
import com.anagrams.project.mapper.EntityMapper;
import com.anagrams.project.model.AnagramGet;
import com.anagrams.project.repository.AnagramRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by Alestar on 4/29/2019.
 */
@Service
public class AnagramService {



    @Autowired
    AnagramRepository anagramRepository;

    @Autowired
    EntityMapper entityMapper;

    @Autowired
    StatsService statsService;

    private static Logger logger = Logger.getLogger(AnagramService.class.getName());

    public AnagramService(){}

    //-------------------------------------------------------------------------------------------------------------------
    //                                          ADD
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * (POST /words.json: Takes a JSON array of English-language words and adds them to the corpus (data store).
     * @param words to be added to respective anagrams in data tore
     */
    public boolean addWordsAsAnagram(List<String> words) {
        boolean added = false;

        for (String word : words) {
            if (Strings.isBlank(word)|| StringUtils.isNumeric(word)) {
                return false;
            }
            String token = generateAnagramToken(word);
            Anagram anagram = anagramRepository.findByToken(token);

            if (anagram != null) {
                Set<String> wordSet = Arrays.stream(anagram.getWords().split(", ")).collect(Collectors.toSet());
                if (wordSet.contains(word)) {
                    added = false;
                } else {
                    wordSet.add(word);
                    anagram.setWords(wordSet.toString());
                }
            } else {
                anagram = new Anagram();
                anagram.setLength(word.length());
                anagram.setToken(token);
                anagram.setWords(word);
                added = true;
            }
            anagramRepository.save(anagram);

        }
        return added;
    }

    //-------------------------------------------------------------------------------------------------------------------
    //                                          FETCH
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * This endpoint should support an optional query param that indicates the maximum number of results to return.     *
     * @param word  used to determine anagram token and look in the map
     * @param limit used to determine how many words would be returned in result, if -1 or bigger than available words return ALL
     * @return Set of anagrams word if available, otherwise return NULL
     */
    public AnagramGet fetchAnagramsOfWord(String word, int limit, boolean permitPN) {// limit resultSet
        logger.info("fetching anagram with limit = " + limit);
        logger.info("fetching anagram with permitPN = " + permitPN);

        List<String> result = new ArrayList<>();
        List<String> words;
        if (word == null || word.isEmpty() || limit < 0) //If the word is invalid or map is empty (i.e after deletion) short circuit exit
            return new AnagramGet(result);

        Anagram anagram = anagramRepository.findByToken(generateAnagramToken(word));
        if(anagram == null)//No anagram found in the DB
            return new AnagramGet(result);

        words = convertWordsToList(anagram.getWords());
        if (words.isEmpty())//If not words found for the word param, short circuit exit
            return new AnagramGet(result);

        if (limit == 0 && permitPN) {//If no limit and Proper Noun are permitted,
            result.addAll(words);//Add all,
            result.remove(word);//but remove the word passed as param, since it should not be part of the resulting anagrams found for that word
        } else if (limit > 0) {// Else if a valid limit number is pass
            for (String w : words) {
                if (!w.equals(word) && (Character.isLowerCase(w.charAt(0)) || permitPN)) {// Only add lower case words or Proper Noun if permitted (Avoid adding the word pass as param)
                    if (result.size() >= limit) //when reach limit break exit
                        break;
                    else
                        result.add(w);
                }
            }
        } else {// No limit(limit == 0) and No Proper Noun (permitPN == false)
            for (String w : words) {
                if (!w.equals(word) && Character.isLowerCase(w.charAt(0))) {//Add lowercase words only (Avoid adding the word pass as param)
                    result.add(w);
                }
            }
        }

        AnagramGet anagramGet = new AnagramGet(result);
        return anagramGet;
    }

    /**
     * Endpoint that identifies words with the most anagrams
     *
     * @return Set of words with most anagrams
     */
    public AnagramGet fetchMostAnagramsWords() {
        String words = anagramRepository.getMaxVolume();
        return new AnagramGet(convertWordsToList(words));
    }

    /**
     * Endpoint to return all anagram groups of size >= x     *
     * @param size of the group of anagrams
     * @return a set of anagram words that are of the same size
     */
    public List<String> fetchAnagramGroupOfSize(int size) {
        List<Anagram> anagramList = anagramRepository.findAllByLengthGreaterThanEqual(size);
        return anagramList.stream().map(Anagram::getWords).collect(Collectors.toList());
    }

    /**
     * Endpoint that takes a set of words and returns whether or not they are all anagrams of each other     *
     * @param words to determinate if ALL are anagrams
     * @return true/false if ALL words are anagrams of each other
     */
    public boolean areAnagrams(List<String> words) {
        if (words == null || words.isEmpty())
            return false;
        String token = generateAnagramToken((String) words.toArray()[0]);
        for (String w : words)
            if (!generateAnagramToken(w).equals(token))//Return false as soon as the first difference between tokens occur
                return false;
        return true;
    }

    //-------------------------------------------------------------------------------------------------------------------
    //                                          DELETE
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Delete ALL information in maps     *
     */
    public boolean deleteAll() {
        boolean success;
        try {
            anagramRepository.deleteAll();
            success= true;
        } catch (Exception e) {
            e.printStackTrace();
            success= false;
        }
        return success;
    }

    /**
     * DELETE /words/:word.json: Deletes a single word from the data store.     *
     * @param word to be deleted if found in data storage
     * @return true/false if a word is found and remove successfully
     */
    public boolean deleteWord(String word) {
        boolean result = false;
        String token = generateAnagramToken(word);

        Anagram anagram = anagramRepository.findByToken(token);
        if (null != anagram) {
            List<String> words = convertWordsToList(anagram.getWords());
            words.remove(word);
            anagram.setWords(words.toString());

            anagramRepository.save(anagram);
            result=true;
        }

        return result;
    }

    /**
     * Endpoint to delete a word and all of its anagrams     *
     * @param word to delete all anagrams from
     */
    public boolean deleteAnagramsOfWord(String word) {
        String token = generateAnagramToken(word);
        Long result = anagramRepository.removeAnagramByToken(token);
        return result!=null;
    }

    //-------------------------------------------------------------------------------------------------------------------
    //                                          AUX
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Generate a token from a word, used for quick lookup of anagrams of that word
     *
     * @param word to generate anagram token from
     * @return a token to determining anagrams of a word
     */
    public String generateAnagramToken(String word) {
        if (word == null || word.isEmpty())
            return "";
        String w = word.replaceAll("\\s", "");
        char[] arrW1 = w.toLowerCase().toCharArray();
        Arrays.sort(arrW1);
        return String.valueOf(arrW1);
    }

    /**
     * Auxiliary method to convert a String of comma separated words into a List
     * @param words as a String of comma separated words
     * @return a list of words
     */
    private List<String> convertWordsToList(String words) {
        return new ArrayList<>(Arrays.asList(words.replaceAll("\\[|]", "").split(", ")));
    }
}
