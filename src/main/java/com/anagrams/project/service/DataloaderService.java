package com.anagrams.project.service;

import com.anagrams.project.entity.Anagram;
import com.anagrams.project.exception.IncorrectAnagramException;
import com.anagrams.project.mapper.EntityMapper;
import com.anagrams.project.repository.AnagramRepository;
import com.anagrams.project.util.FileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.logging.Logger;

@Component
public class DataloaderService {


    @Autowired
    AnagramRepository anagramRepository;

    @Autowired
    EntityMapper entityMapper;

    @Autowired
    AnagramService anagramService;

    @Autowired
    DataloaderService dataloaderService;

    @Autowired
    private FileReader fileReader;

    private static Logger logger= Logger.getLogger(AnagramService.class.getName());

    private Map<String,Set<String>> tokenToWordsMap= new HashMap<>();

    private boolean loadDicFile(){
        boolean success=false;
        if(anagramRepository.count() <= 0) {//If the table is empty
            fileReader.readDictionaryFile();
            try {
                List<String> linesList = fileReader.getListOfLines();
                dataloaderService.populateDatabase(linesList);
                success= true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return success;
    }

    public void populateDatabase(List<String> words) throws IncorrectAnagramException {
        for (String w : words) {
            addWordAsAnagramToMap(w);
        }
        logger.info("The tokenToWordsMap: " + tokenToWordsMap.toString());
        List<Anagram> anagramList = entityMapper.mapper(tokenToWordsMap);
        anagramRepository.saveAll(anagramList);
    }

    private void addWordAsAnagramToMap(String word) throws IncorrectAnagramException {

        if(word == null || word.isEmpty()){
            throw new IncorrectAnagramException();
        }
        String token = anagramService.generateAnagramToken(word);
        //add the word to the token-words map
        if(tokenToWordsMap.containsKey(token)){
            tokenToWordsMap.get(token).add(word);
        }
        else{
            Set<String> words= new HashSet<>();
            words.add(word);
            tokenToWordsMap.put(token, words);
        }
        //stats.updateMostAnagramWords(tokenToWordsMap.get(token).size(),token);//update the MostAnagramWords counter and token correspondingly*/

    }

    @PostConstruct
    public boolean init(){
        return loadDicFile();
    }
}
