package com.anagrams.project.service;

import com.anagrams.project.exception.IncorrectAnagramException;
import com.anagrams.project.model.StatsResource;
import com.anagrams.project.util.Stats;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Alestar on 4/29/2019.
 */
@Component
public class AnagramService {
    private static Logger logger= Logger.getLogger(AnagramService.class.getName());
    private Stats stats= new Stats();
    private Map<Integer,Set<String>> lengthToTokensMap= new HashMap<>();
    private Map<String,Set<String>> tokenToWordsMap= new HashMap<>();

    public AnagramService(){}

    //-------------------------------------------------------------------------------------------------------------------
    //                                          GETTERS
    //-------------------------------------------------------------------------------------------------------------------
    public Stats getStats() {
        return stats;
    }

    public Map<Integer, Set<String>> getLengthToTokensMap() {
        return lengthToTokensMap;
    }

    public Map<String, Set<String>> getTokenToWordsMap() {
        return tokenToWordsMap;
    }

    //-------------------------------------------------------------------------------------------------------------------
    //                                          TOKEN
    //-------------------------------------------------------------------------------------------------------------------
    /**
     * Generate a token from a word, used for quick lookup of anagrams of that word
     * @param word to generate anagram token from
     * @return a token to determining anagrams of a word
     */
    public String generateAnagramToken(String word) {
        if(word==null || word.isEmpty())
            return "";
        String w = word.replaceAll("\\s", "");
        char[] arrW1 = w.toLowerCase().toCharArray();
        Arrays.sort(arrW1);
        return String.valueOf(arrW1);
    }

    //-------------------------------------------------------------------------------------------------------------------
    //                                          ADD
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * (POST /words.json: Takes a JSON array of English-language words and adds them to the corpus (data store).
     * @param word to be added to respective anagrams in data tore
     * @throws IncorrectAnagramException for word not being a valid input
     */
    private void addWordAsAnagram(String word) throws IncorrectAnagramException {

        if(word == null || word.isEmpty()){
            throw new IncorrectAnagramException(word);
        }

        String token = generateAnagramToken(word);
        int length = word.length();
        //First add the token to the length-tokens map
        if(lengthToTokensMap.containsKey(length)){
            lengthToTokensMap.get(length).add(token);
        }
        else{
            Set<String> tokens= new HashSet<>();
            tokens.add(token);
            lengthToTokensMap.put(length, tokens);
            stats.addToSumWordLengths(length);// Add new length to the sum (for calculating average)

        }
        //Second, add the word to the token-words map
        if(tokenToWordsMap.containsKey(token)){
            tokenToWordsMap.get(token).add(word);
        }
        else{
            Set<String> words= new HashSet<>();
            words.add(word);
            tokenToWordsMap.put(token, words);
        }
        stats.incrementTotalWords();// Increment by total amount of word
        stats.updateMostAnagramWords(tokenToWordsMap.get(token).size(),token);//update the MostAnagramWords counter and token correspondingly

    }

    /**
     * Add list of words as anagrams
     * @param words List to add as anagrams
     * @throws IncorrectAnagramException to be handle when a word is invalid
     */
    public void addWordsAsAnagram(List<String> words) throws IncorrectAnagramException{
        for(String w: words){
            addWordAsAnagram(w);
        }
        logger.info("The lengthToTokensMap: " + lengthToTokensMap.toString() );
        logger.info("The tokenToWordsMap: " + tokenToWordsMap.toString() );
    }

    //-------------------------------------------------------------------------------------------------------------------
    //                                          FETCH
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * This endpoint should support an optional query param that indicates the maximum number of results to return.
     * @param word used to determine anagram token and look in the map
     * @param limit used to determine how many words would be returned in result, if -1 or bigger than available words return ALL
     * @return Set of anagrams word if available, otherwise return NULL
     */
    public Set<String> fetchAnagramsOfWord(String word, int limit, boolean permitPN){// limit resultSet
        logger.info("fetching anagram with limit = " + limit);
        logger.info("fetching anagram with permitPN = " + permitPN);

        Set<String> result = new HashSet<>();
        Set<String> words;
        if (word == null || word.isEmpty() || tokenToWordsMap == null || tokenToWordsMap.isEmpty() || limit<0) //If the word is invalid or map is empty (i.e after deletion) short circuit exit
            return result;

        words = tokenToWordsMap.get(generateAnagramToken(word));
        if(words == null || words.isEmpty())//If not words found for the word param, short circuit exit
            return result;

        if (limit == 0 && permitPN) {//If no limit and Proper Noun are permitted,
            result.addAll(words);//Add all,
            result.remove(word);//but remove the word passed as param, since it should not be part of the resulting anagrams found for that word
        }
        else if(limit > 0) {// Else if a valid limit number is pass
                for (String w : words) {
                    if (!w.equals(word) && (Character.isLowerCase(w.charAt(0)) || permitPN)) {// Only add lower case words or Proper Noun if permitted (Avoid adding the word pass as param)
                        if (result.size() >= limit) //when reach limit break exit
                            break;
                        else
                        result.add(w);
                    }
                }
        }
        else {// No limit(limit == 0) and No Proper Noun (permitPN == false)
                for (String w : words) {
                    if (!w.equals(word) && Character.isLowerCase(w.charAt(0))) {//Add lowercase words only (Avoid adding the word pass as param)
                        result.add(w);
                    }
                }
        }
        return result;
    }

    /**
     * (GET /anagrams/:word.json:
     * @param word used to generate anagram token, used in the lookup
     * @return List of words if found token for given word, or NULL if not
     */
    public Set<String> fetchAnagramsOfWord(String word){
        return fetchAnagramsOfWord(word,0, true);
    }

    /**
     * Endpoint that takes a set of words and returns whether or not they are all anagrams of each other
     * @param words to determinate if ALL are anagrams
     * @return true/false if ALL words are anagrams of each other
     */
    public boolean areAnagrams(List<String> words){

        if(words == null || words.isEmpty())
            return false;
        String token= generateAnagramToken((String)words.toArray()[0]);
        for (String w: words)
            if(!generateAnagramToken(w).equals(token))//Return false as soon as the first difference between tokens occur
                return false;
        return true;

    }

    /**
     *  Endpoint that returns a count of words in the corpus and min/max/median/average word length (a.k.a Stats)
     * @return a Stats object if there is data to calculate other wise return null
     */
    public StatsResource fetchStatsResource(){
        if(!lengthToTokensMap.isEmpty()) {
            stats.calculateStats(lengthToTokensMap.keySet());
        }
        return new StatsResource(stats);
    }

    /**
     * Endpoint that identifies words with the most anagrams
     * @return Set of words with most anagrams
     */
    public Set<String> fetchMostAnagramsWords(){
        return tokenToWordsMap.get(stats.getMostAnagramsToken());
    }

    /**
     * Endpoint to return all anagram groups of size >= x
     * @param size of the group of anagrams
     * @return a set of anagram words that are of the same size
     */
    public Set<String> anagramGroupOfSize(int size){

        Set<String> anagrams = new HashSet<>();
        Set<String> tokens = lengthToTokensMap.get(size);
        if(tokens != null && !tokens.isEmpty()) {
            for (String t : tokens) {
                anagrams.addAll(tokenToWordsMap.get(t));
            }
        }
        return  anagrams;
    }

    //-------------------------------------------------------------------------------------------------------------------
    //                                          DELETE
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * DELETE /words/:word.json: Deletes a single word from the data store.
     * @param word to be deleted if found in data storage
     * @return true/false if a word is found and remove successfully
     */
    public boolean deleteWord(String word){
        boolean result= false;
        String token = generateAnagramToken(word);
        if(tokenToWordsMap.containsKey(token)){//Check if token for that word exist in the map
            Set<String> words = tokenToWordsMap.get(token);
            result = words.remove(word);
            if (words.isEmpty()) {
                tokenToWordsMap.remove(token);//If there are no words left (last one got deleted) remove that token key entry as well
                deleteTokenFromLengthMap(word.length(),token); //If the token key entry was deleted in onw Map, this needs to be reflected in the other
            }
        }
        return result;
    }


    /**
     * Deletes a single token from the data store.
     * @param length of the word used as key in the map
     * @param token to be deleted in the set of tokens available
     * @return true/false when attempt to delete a token from the token set for a word length in the map
     */
    private boolean deleteTokenFromLengthMap(int length, String token){
        boolean result= false;
        if(lengthToTokensMap.containsKey(length)){
            Set<String> tokens=  lengthToTokensMap.get(length);
            result = tokens.remove(token);
        }
        return result;
    }

    /**
     * Delete ALL information in maps     *
     */
    public void deleteALL() {//Implement specific exception
        lengthToTokensMap.clear();
        tokenToWordsMap.clear();
        stats = new Stats();
    }

    /**
     * Endpoint to delete a word and all of its anagrams
     * @param word to delete all anagrams from
     */
    public boolean deleteAllAnagramsOfWord(String word){
        String token = generateAnagramToken(word);
        if(tokenToWordsMap.remove(token, tokenToWordsMap.get(token))){// Remove all words for that anagram token and token key
            Set<String> tokens = lengthToTokensMap.get(word.length());
            tokens.remove(token);// Remove the anagram token itself from the other map
            return true;
        }
        return false;

    }
}
