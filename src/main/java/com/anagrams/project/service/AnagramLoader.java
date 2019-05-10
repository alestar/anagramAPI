package com.anagrams.project.service;

import com.anagrams.project.util.FileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class AnagramLoader {

    private AnagramService anagramService;
    private FileReader fileReader;

    @Autowired
    public void setAnagramService(AnagramService anagramService) {
        this.anagramService = anagramService;
    }
    @Autowired
    public void setFileReader(FileReader fileReader){
        this.fileReader = fileReader;
    }

    private void loadMapWithDicFile(){
        fileReader.readDictionaryFile();
        try {
            List<String> linesList = fileReader.getListOfLines();
            anagramService.addWordsAsAnagram(linesList);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @PostConstruct
    public void init(){
       loadMapWithDicFile();
    }


}
