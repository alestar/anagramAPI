package com.anagrams.project.service;

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

    /**
     *
     * @param word to generate anagram token from
     * @return a token to determining anagrams of a word
     */
    private String generateAnagramToken(String word) {
        if(word==null || word.isEmpty())
            return "";
        String w = word.replaceAll("\\s", "");
        char[] arrW1 = w.toLowerCase().toCharArray();
        Arrays.sort(arrW1);
        return String.valueOf(arrW1);
    }

    /**
     * Determinate if 2 words are anagram (Extra endpoint to add)
     * @param word1 to compare
     * @param word2 to compare
     * @return true/false depending of 2 words being anagrams or not
     */
    public boolean isAnagram(String word1, String word2) {
        String w1 = word1.replaceAll("\\s", "");
        String w2 = word2.replaceAll("\\s", "");
        if (w1.length() != w2.length()) {
            return false;
        } else {
            char[] arrW1 = w1.toLowerCase().toCharArray();
            char[] arrW2 = w2.toLowerCase().toCharArray();
            Arrays.sort(arrW1);
            Arrays.sort(arrW2);
            return Arrays.equals(arrW1, arrW2);
        }
    }

    /**
     * (1.1) POST /words.json: Takes a JSON array of English-language words and adds them to the corpus (data store).
     * @param word to be added to respective anagrams in data tore
     * @throws Exception for word not being a valid input
     */
    private void addWordAsAnagram(String word) throws Exception {

        if(word == null || word.isEmpty()){
            throw new Exception("Word can't be null or empty, please provide a valid word");
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

    public void addWordsAsAnagram(List<String> words) throws Exception{
        for(String w: words){
            addWordAsAnagram(w);
        }
        logger.info("The lengthToTokensMap: " + lengthToTokensMap.toString() );
        logger.info("The tokenToWordsMap: " + tokenToWordsMap.toString() );
    }

    /**
     * (1.2) GET /anagrams/:word.json:
     * @param word used to generate anagram token, used in the lookup
     * @return List of words if found token for given word, or NULL if not
     */
    public Set<String> getAnagramsOfWord(String word){
        return fetchAnagramsOfWord(word,-1, true);
    }

    /**
     * (1.3) This endpoint should support an optional query param that indicates the maximum number of results to return.
     * @param word used to determine anagram token and look in the map
     * @param limit used to determine how many words would be returned in result, if -1 or bigger than available words return ALL
     * @return Set of anagrams word if available, otherwise return NULL
     */
    public Set<String> fetchAnagramsOfWord(String word, int limit, boolean permitPN){// limit resultSet
        logger.info("fetching anagram with limit = " + limit);
        logger.info("fetching anagram with permitPN = " + permitPN);

        Set<String> result = new HashSet<>();
        Set<String> words;
        if (tokenToWordsMap == null || tokenToWordsMap.isEmpty()) {
            return result;
        }
        else {
            words = tokenToWordsMap.get(generateAnagramToken(word));
            if (limit == -1) {
                if (permitPN) {
                    return tokenToWordsMap.get(generateAnagramToken(word));//Return all
                } else {
                    for (String w : words) {
                        if (Character.isLowerCase(w.charAt(0))) {// Only add lower case
                            result.add(w);
                        }
                    }
                }
            } else {// A valid limit number is pass
                for (String w : words) {
                    if ((Character.isLowerCase(w.charAt(0)) || permitPN)) {
                        result.add(w);
                        if (result.size() >= limit)
                            break;
                    }
                }
            }
        }
        return result;

    }

    /**
     * (1.4) DELETE /words/:word.json: Deletes a single word from the data store.
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
    public void deleteALL() throws Exception{//Implement specific exception
        lengthToTokensMap.clear();
        tokenToWordsMap.clear();
        stats = new Stats();
    }

    /**
     *  (2.1) Endpoint that returns a count of words in the corpus and min/max/median/average word length (a.k.a Stats)
     * @return a Stats object if there is data to calculate other wise return null
     */
    public StatsResource fetchStatsResource(){
        if(!lengthToTokensMap.isEmpty()) {
            stats.calculateStats(lengthToTokensMap.keySet());
        }
        return new StatsResource(stats);
    }

    /**
     * (2.2) Respect a query param for whether or not to include proper nouns in the list of anagrams
     * @param word to find anagrams of
     * @param includePN to indicate if include or not proper nouns that are anagrams of the word
     * @return a set of anagrams of the word
     */
    public Set<String> getAnagramsOfWordIncludePN(String word, boolean includePN){// limit resultSet inclusive

        Set<String> result= new HashSet<>();
        Set<String> words = tokenToWordsMap.get(generateAnagramToken(word));
        for (String w: words) {
            if(Character.isLowerCase(w.charAt(0)) || includePN)
                result.add(w);
        }
        return result;

    }

    /**
     * (2.3) Endpoint that identifies words with the most anagrams
     * @return Set of words with most anagrams
     */
    public Set<String> fetchMostAnagramsWords(){
        return tokenToWordsMap.get(stats.getMostAnagramsToken());
    }

    /**
     * (2.4) Endpoint that takes a set of words and returns whether or not they are all anagrams of each other
     * @param words to determinate if ALL are anagrams
     * @return true/false if ALL words are anagrams of each other
     */
    public boolean areAnagrams(List<String> words){
        String token= generateAnagramToken((String)words.toArray()[0]);
        for (String w: words)
            if(!generateAnagramToken(w).equals(token))//Return false as soon as the first difference between tokens occur
                return false;
        return true;

    }

    /**
     * (2.5) Endpoint to return all anagram groups of size >= x
     * @param size of the group of anagrams
     * @return a set of anagram words that are of the same size
     */
    public Set<String> anagramGroupOfSize(int size){

        Set<String> anagrams = new HashSet<>();
        Set<String> tokens = lengthToTokensMap.get(size);
        for (String t: tokens) {
            anagrams.addAll(tokenToWordsMap.get(t));
        }
        return  anagrams;
    }

    /**
     * (2.6) Endpoint to delete a word and all of its anagrams
     * @param word to delete all anagrams from
     */
    public boolean deleteAllAnagramsOfWord(String word){
        String token = generateAnagramToken(word);
        if(tokenToWordsMap.remove(token, tokenToWordsMap.get(token))){// Remove all words for that anagram token
            Set<String> tokens = lengthToTokensMap.get(word.length());
            tokens.remove(token);// Remove the anagram token itself from the other map
            return true;
        }
        return false;

    }
}
